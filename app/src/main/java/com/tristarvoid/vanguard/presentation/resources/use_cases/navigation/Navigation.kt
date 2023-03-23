package com.tristarvoid.vanguard.presentation.resources.use_cases.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tristarvoid.vanguard.domain.use_cases.navigation.NavViewModel
import com.tristarvoid.vanguard.presentation.resources.screens.*
import com.tristarvoid.vanguard.presentation.resources.views.Calendar

@Composable
fun Navigation(
    padding: PaddingValues,
    navControl: NavHostController,
    navViewModel: NavViewModel
) {
    NavHost(navController = navControl, startDestination = ScreenConfiguration.HomeScreen.route) {
        //Primary
        composable(route = ScreenConfiguration.HomeScreen.route) {
            navViewModel.heading.value = "Home"
            navViewModel.concernedItem.value = 0
            Home(padding)
        }
        composable(route = ScreenConfiguration.WorkoutScreen.route) {
            navViewModel.heading.value = "Workouts"
            navViewModel.concernedItem.value = 1
            Workouts(padding)
        }
        composable(route = ScreenConfiguration.DecisionsScreen.route) {
            navViewModel.heading.value = "Decisions"
            navViewModel.concernedItem.value = 2
            Decisions(padding)
        }
        composable(route = ScreenConfiguration.RemindersScreen.route) {
            navViewModel.heading.value = "Reminders"
            navViewModel.concernedItem.value = 3
            Reminders(padding)
        }
        composable(route = ScreenConfiguration.NutritionScreen.route) {
            navViewModel.heading.value = "Nutrition"
            navViewModel.concernedItem.value = 4
            Nutrition(padding)
        }
        composable(route = ScreenConfiguration.WaterScreen.route) {
            navViewModel.heading.value = "Water"
            navViewModel.concernedItem.value = 5
            Water(padding)
        }
        composable(route = ScreenConfiguration.SettingsScreen.route) {
            navViewModel.heading.value = "Settings"
            navViewModel.concernedItem.value = 6
            Settings(padding)
        }
        composable(route = ScreenConfiguration.PrivacyScreen.route) {
            navViewModel.heading.value = "Privacy"
            navViewModel.concernedItem.value = 7
            Privacy(padding)
        }
        composable(route = ScreenConfiguration.AboutScreen.route) {
            navViewModel.heading.value = "About"
            navViewModel.concernedItem.value = 8
            About(padding)
        }

        //Secondary
        composable(route = ScreenConfiguration.CalendarView.route) {
            navViewModel.heading.value = "History"
            Calendar(navControl)
        }
    }
}