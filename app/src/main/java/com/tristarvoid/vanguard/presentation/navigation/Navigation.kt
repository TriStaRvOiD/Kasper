package com.tristarvoid.vanguard.presentation.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.presentation.nav_screens.*
import com.tristarvoid.vanguard.presentation.onboarding.WelcomeScreen
import com.tristarvoid.vanguard.presentation.views.Calendar
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navControl: NavHostController,
    navViewModel: NavViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope,
    defaultScreen: String
) {
    NavHost(
        navController = navControl,
        startDestination = defaultScreen
    ) {
        //Onboard
        composable(route = ScreenConfiguration.WelcomeScreen.route) {
            WelcomeScreen(navController = navControl)
        }

        //Primary
        composable(route = ScreenConfiguration.HomeScreen.route) {
            navViewModel.heading.value = "Home"
            navViewModel.concernedItem.value = 0
            Home(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.WorkoutScreen.route) {
            navViewModel.heading.value = "Workouts"
            navViewModel.concernedItem.value = 1
            Workouts(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.DecisionsScreen.route) {
            navViewModel.heading.value = "Decisions"
            navViewModel.concernedItem.value = 2
            Decisions(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.RemindersScreen.route) {
            navViewModel.heading.value = "Reminders"
            navViewModel.concernedItem.value = 3
            Reminders(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.NutritionScreen.route) {
            navViewModel.heading.value = "Nutrition"
            navViewModel.concernedItem.value = 4
            Nutrition(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.WaterScreen.route) {
            navViewModel.heading.value = "Water"
            navViewModel.concernedItem.value = 5
            Water(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.SettingsScreen.route) {
            navViewModel.heading.value = "Settings"
            navViewModel.concernedItem.value = 6
            Settings(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.PrivacyScreen.route) {
            navViewModel.heading.value = "Privacy"
            navViewModel.concernedItem.value = 7
            Privacy(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.AboutScreen.route) {
            navViewModel.heading.value = "About"
            navViewModel.concernedItem.value = 8
            About(navControl, navViewModel, drawerState, scope)
        }

        //Secondary
        composable(route = ScreenConfiguration.CalendarView.route) {
            navViewModel.heading.value = "History"
            Calendar(navControl)
        }
    }
}