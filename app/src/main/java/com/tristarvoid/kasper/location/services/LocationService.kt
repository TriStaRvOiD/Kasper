/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.location.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import com.google.android.gms.location.Priority
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.location.data.LocationServiceStatusDataStoreRepository
import com.tristarvoid.kasper.location.data.LocationClient
import com.tristarvoid.kasper.io.data.IODao
import com.tristarvoid.kasper.location.data.LocationDao
import com.tristarvoid.kasper.location.utils.Polygon
import com.tristarvoid.kasper.location.utils.RTree
import com.tristarvoid.kasper.core.presentation.utils.Region
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationDao: LocationDao

    @Inject
    lateinit var ioDao: IODao

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var statusRepository: LocationServiceStatusDataStoreRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val notification by lazy {
        Notification.Builder(this, "location_tracking_service_channel_id")
            .setContentTitle("Location tracking is active")
            .setContentText("Kasper is detecting your location in the background.")
            .setShowWhen(false)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_LOCATION_TRACKING_ENABLE -> start()
            ACTION_LOCATION_TRACKING_DISABLE -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val rTree = RTree()

        //Populate this list
        val polygons = listOf(
            Polygon(
                listOf(
                    Region(0.0, 0.0),
                    Region(0.0, 0.0),
                    Region(0.0, 0.0),
                    Region(0.0, 0.0)
                )
            ),
        )

        for (polygon in polygons) {
            rTree.addPolygon(polygon)
        }

        locationClient
            .getLocationUpdates(10000L, Priority.PRIORITY_HIGH_ACCURACY)
            .catch { e ->
                e.printStackTrace()
            }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                locationDao.insertEntry(
                    latitude = lat,
                    longitude = long,
                    date = LocalDate.now().toEpochDay()
                )
                val ioResult = rTree.search(lat, long)
                ioDao.insertEntry(
                    LocalDate.now().toEpochDay(),
                    ioResult,
                    LocalTime.now().toNanoOfDay()
                )
            }
            .launchIn(serviceScope)
        startForeground(2, notification)
        alterListeningStatus(value = true)
    }

    private fun stop() {
        serviceScope.launch {
            ioDao.insertEntry(LocalDate.now().toEpochDay(), null, LocalTime.now().toNanoOfDay())
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        alterListeningStatus(value = false)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun alterListeningStatus(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            statusRepository.saveListeningState(value = value)
        }
    }

    companion object {
        const val ACTION_LOCATION_TRACKING_ENABLE = "ACTION_LOCATION_TRACKING_ENABLE"
        const val ACTION_LOCATION_TRACKING_DISABLE = "ACTION_LOCATION_TRACKING_DISABLE"
    }
}