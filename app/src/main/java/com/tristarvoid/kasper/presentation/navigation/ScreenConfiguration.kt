/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.navigation

sealed class ScreenConfiguration(val route: String) {
    //Loading
    object LoadingScreen : ScreenConfiguration(route = "loading_screen")
    //Onboard
    object WelcomeScreen : ScreenConfiguration(route = "welcome_screen")

    //Primary
    object HomeScreen : ScreenConfiguration(route = "home_screen")
    object WorkoutScreen : ScreenConfiguration(route = "workouts_screen")
    object RemindersScreen : ScreenConfiguration(route = "reminders_screen")
    object NutritionScreen : ScreenConfiguration(route = "nutrition_screen")
    object TimerScreen : ScreenConfiguration(route = "timer_screen")
    object BMIScreen : ScreenConfiguration(route = "bmi_screen")
    object SettingsScreen : ScreenConfiguration(route = "settings_screen")
    object AboutScreen : ScreenConfiguration(route = "about_screen")
    object LicensesScreen : ScreenConfiguration(route = "licenses_screen")

    //Secondary
    object CalendarView : ScreenConfiguration(route = "calendar_view")
}