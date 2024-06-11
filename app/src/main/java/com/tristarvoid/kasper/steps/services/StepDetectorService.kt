/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.IBinder
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toIcon
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.steps.data.StepServiceStatusDataStoreRepository
import com.tristarvoid.kasper.sensor.data.MeasurableSensor
import com.tristarvoid.kasper.steps.data.StepsDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class StepDetectorService : Service() {

    @Inject
    lateinit var stepsDao: StepsDao

    @Inject
    lateinit var measurableSensor: MeasurableSensor

    @Inject
    lateinit var statusRepository: StepServiceStatusDataStoreRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val notification by lazy {
        Notification.Builder(this, "step_counting_service_channel_id")
            .setContentTitle("Step counting is active")
            .setContentText("Kasper is detecting your step count in the background.")
            .setShowWhen(false)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null)
            when (intent.action) {
                ACTION_STEP_TRACKING_ENABLE -> start()
                ACTION_STEP_TRACKING_DISABLE -> stop()
            }
        else
            stop()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        measurableSensor.startListening()
        measurableSensor.setOnSensorValuesChangedListener {
            serviceScope.launch {
                stepsDao.insertRawEntry(LocalTime.now().toNanoOfDay(), LocalDate.now().toEpochDay())
            }
        }

        startForeground(1, notification)
        alterListeningStatus(value = true)
    }

    private fun stop() {
        measurableSensor.stopListening()
        alterListeningStatus(value = false)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
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
        const val ACTION_STEP_TRACKING_ENABLE = "ACTION_STEP_TRACKING_ENABLE"
        const val ACTION_STEP_TRACKING_DISABLE = "ACTION_STEP_TRACKING_DISABLE"
    }
}