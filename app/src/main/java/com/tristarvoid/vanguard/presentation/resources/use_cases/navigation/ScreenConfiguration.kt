package com.tristarvoid.vanguard.presentation.resources.use_cases.navigation

sealed class ScreenConfiguration(val route: String) {
    //Primary
    object HomeScreen : ScreenConfiguration("home_screen")
    object WorkoutScreen : ScreenConfiguration("workouts_screen")
    object DecisionsScreen : ScreenConfiguration("decisions_screen")
    object RemindersScreen : ScreenConfiguration("reminders_screen")
    object NutritionScreen : ScreenConfiguration("nutrition_screen")
    object WaterScreen : ScreenConfiguration("h2o_screen")
    object SettingsScreen : ScreenConfiguration("settings_screen")
    object PrivacyScreen : ScreenConfiguration("privacy_screen")
    object AboutScreen : ScreenConfiguration("about_screen")

    //Secondary
    object CalendarView : ScreenConfiguration("calendar_view")
}