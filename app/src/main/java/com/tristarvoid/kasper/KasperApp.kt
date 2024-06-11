/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class KasperApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val stepCountingChannel by lazy {
            NotificationChannel(
                "step_counting_service_channel_id",
                "Step counting",
                NotificationManager.IMPORTANCE_LOW
            )
        }
        stepCountingChannel.description = "Notification for the step counting foreground service"

        val remindersChannel by lazy {
            NotificationChannel(
                "reminders_channel_id",
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
        }
        remindersChannel.description = "Notifications for the 'Reminders' feature."

        val locationTrackingChannel by lazy {
            NotificationChannel(
                "location_tracking_service_channel_id",
                "Location Tracking",
                NotificationManager.IMPORTANCE_HIGH
            )
        }
        locationTrackingChannel.description = "Notification for the 'I/O detection' and 'Map Tracking' features"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(stepCountingChannel)
        notificationManager.createNotificationChannel(remindersChannel)
        notificationManager.createNotificationChannel(locationTrackingChannel)
    }
}