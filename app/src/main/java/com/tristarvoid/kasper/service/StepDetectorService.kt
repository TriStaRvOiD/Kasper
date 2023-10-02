/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.data.datastore.StatusDataStoreRepository
import com.tristarvoid.kasper.data.sensor.MeasurableSensor
import com.tristarvoid.kasper.data.steps.StepsDao
import com.tristarvoid.kasper.data.steps.StepsData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class StepDetectorService : Service() {

    @Inject
    lateinit var stepsDao: StepsDao

    @Inject
    lateinit var measurableSensor: MeasurableSensor

    @Inject
    lateinit var statusRepository: StatusDataStoreRepository

    private val _date = MutableStateFlow(value = LocalDate.now().toEpochDay())

    private val notification by lazy {
        Notification.Builder(this, "foreground_service_channel_id")
            .setContentTitle("Step counting is active")
            .setContentText("Kasper is detecting your step count in the background.")
            .setShowWhen(false)
            .setSmallIcon(R.drawable.notification_logo)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        measurableSensor.startListening()
        measurableSensor.setOnSensorValuesChangedListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (stepsDao.isEntryPresent(date = _date.value))
                    stepsDao.incrementStepCount(date = _date.value)
                else {
                    val data = StepsData(
                        stepCount = 0,
                        id = _date.value
                    )
                    stepsDao.upsertEntry(stepsData = data)
                }
            }
        }

        startForeground(1, notification)
        alterListeningStatus(value = true)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        measurableSensor.stopListening()
        super.onDestroy()
        alterListeningStatus(value = false)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun alterListeningStatus(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            statusRepository.saveListeningState(value = value)
        }
    }
}