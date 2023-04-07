/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.presentation.navigation

sealed class ScreenConfiguration(val route: String) {
    //Loading
    object LoadingScreen : ScreenConfiguration("loading_screen")
    //Onboard
    object WelcomeScreen : ScreenConfiguration("welcome_screen")

    //Primary
    object HomeScreen : ScreenConfiguration("home_screen")
    object WorkoutScreen : ScreenConfiguration("workouts_screen")
    object RemindersScreen : ScreenConfiguration("reminders_screen")
    object NutritionScreen : ScreenConfiguration("nutrition_screen")
    object TimerScreen : ScreenConfiguration("timer_screen")
    object BMIScreen : ScreenConfiguration("bmi_screen")
    object CovidScreen : ScreenConfiguration("covid_screen")
    object SettingsScreen : ScreenConfiguration("settings_screen")
    object AboutScreen : ScreenConfiguration("about_screen")

    //Secondary
    object CalendarView : ScreenConfiguration("calendar_view")
}