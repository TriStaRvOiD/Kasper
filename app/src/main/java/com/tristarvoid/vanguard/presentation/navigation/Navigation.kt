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
        //Loading
        composable(route = ScreenConfiguration.LoadingScreen.route) {
            LoadingScreen()
        }
        //Onboard
        composable(route = ScreenConfiguration.WelcomeScreen.route) {
            WelcomeScreen(navController = navControl)
        }

        //Primary
        composable(route = ScreenConfiguration.HomeScreen.route) {
            navViewModel.mainHeading.value = "Home"
            navViewModel.concernedItem.value = 0
            Home(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.WorkoutScreen.route) {
            navViewModel.mainHeading.value = "Workouts"
            navViewModel.concernedItem.value = 1
            Workouts(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.DecisionsScreen.route) {
            navViewModel.mainHeading.value = "Decisions"
            navViewModel.concernedItem.value = 2
            Decisions(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.RemindersScreen.route) {
            navViewModel.mainHeading.value = "Reminders"
            navViewModel.concernedItem.value = 3
            Reminders(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.NutritionScreen.route) {
            navViewModel.mainHeading.value = "Nutrition"
            navViewModel.concernedItem.value = 4
            Nutrition(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.WaterScreen.route) {
            navViewModel.mainHeading.value = "Water"
            navViewModel.concernedItem.value = 5
            Water(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.SettingsScreen.route) {
            navViewModel.mainHeading.value = "Settings"
            navViewModel.concernedItem.value = 6
            Settings(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.PrivacyScreen.route) {
            navViewModel.mainHeading.value = "Privacy"
            navViewModel.concernedItem.value = 7
            Privacy(navControl, navViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.AboutScreen.route) {
            navViewModel.mainHeading.value = "About"
            navViewModel.concernedItem.value = 8
            About(navControl, navViewModel, drawerState, scope)
        }

        //Secondary
        composable(route = ScreenConfiguration.CalendarView.route) {
            navViewModel.fragHeading.value = "History"
            Calendar(navControl, navViewModel)
        }
    }
}