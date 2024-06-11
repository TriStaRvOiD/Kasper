/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tristarvoid.kasper.location.data.LocationServiceStatusDataStoreRepository
import com.tristarvoid.kasper.steps.data.StepServiceStatusDataStoreRepository
import com.tristarvoid.kasper.location.services.LocationService
import com.tristarvoid.kasper.steps.services.StepDetectorService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RestartReceiver : BroadcastReceiver() {

    @Inject
    lateinit var stepsStatusRepository: StepServiceStatusDataStoreRepository

    @Inject
    lateinit var locationStatusRepository: LocationServiceStatusDataStoreRepository

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> handleStepServiceRestart(
                context = context,
                stepsStatusRepository
            )

            Intent.ACTION_BOOT_COMPLETED -> handleLocationServiceRestart(
                context = context,
                locationStatusRepository
            )

            Intent.ACTION_PACKAGE_REPLACED -> {
                handleStepServiceRestart(context = context, stepsStatusRepository)
                handleLocationServiceRestart(
                    context = context,
                    locationStatusRepository
                )
            }
        }
    }
}

private fun handleStepServiceRestart(
    context: Context,
    stepsStatusRepository: StepServiceStatusDataStoreRepository
) {
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + job)
    val stepsServiceIntent = Intent(context, StepDetectorService::class.java)
    val deferredStepsTask = scope.async {
        stepsStatusRepository.readListeningState().collect { isServiceRunning ->
            if (isServiceRunning)
                stepsServiceIntent.apply {
                    action =
                        StepDetectorService.ACTION_STEP_TRACKING_ENABLE
                    context.startService(this)
                }
            else
                stepsServiceIntent.apply {
                    action = StepDetectorService.ACTION_STEP_TRACKING_DISABLE
                    context.startService(this)
                }
        }
    }
    scope.launch {
        deferredStepsTask.await()
        scope.cancel()
    }
}

private fun handleLocationServiceRestart(
    context: Context,
    locationStatusRepository: LocationServiceStatusDataStoreRepository
) {
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + job)
    val locationServiceIntent = Intent(context, LocationService::class.java)
    val deferredLocationTask = scope.async {
        locationStatusRepository.readListeningState().collect { isServiceRunning ->
            if (isServiceRunning)
                locationServiceIntent.apply {
                    action = LocationService.ACTION_LOCATION_TRACKING_ENABLE
                    context.startService(this)
                }
            else
                locationServiceIntent.apply {
                    action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
                    context.startService(this)
                }
        }
    }

    scope.launch {
        deferredLocationTask.await()
        scope.cancel()
    }
}