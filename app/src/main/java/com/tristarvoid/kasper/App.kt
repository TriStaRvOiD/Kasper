/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val foregroundServiceChannel = NotificationChannel(
            "foreground_service_channel_id",
            "Step counting",
            NotificationManager.IMPORTANCE_LOW
        )
        foregroundServiceChannel.description =
            "Notification for the step counting foreground service"

        val remindersChannel = NotificationChannel(
            "reminders_channel_id",
            "Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        remindersChannel.description = "Notifications for the 'Reminders' feature."

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(foregroundServiceChannel)
        notificationManager.createNotificationChannel(remindersChannel)
    }
}