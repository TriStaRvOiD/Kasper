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

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.presentation.onboarding.WelcomeScreen
import com.tristarvoid.kasper.presentation.screens.About
import com.tristarvoid.kasper.presentation.screens.Bmi
import com.tristarvoid.kasper.presentation.screens.Licenses
import com.tristarvoid.kasper.presentation.screens.LoadingScreen
import com.tristarvoid.kasper.presentation.screens.Nutrition
import com.tristarvoid.kasper.presentation.screens.Reminders
import com.tristarvoid.kasper.presentation.screens.Settings
import com.tristarvoid.kasper.presentation.screens.Timer
import com.tristarvoid.kasper.presentation.screens.Workouts
import com.tristarvoid.kasper.presentation.screens.home.Home
import com.tristarvoid.kasper.presentation.views.Calendar
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
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
            WelcomeScreen()
        }

        //Primary
        composable(route = ScreenConfiguration.HomeScreen.route) {
            holderViewModel.mainHeading.value = ""
            holderViewModel.concernedItem.value = 0
            Home(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.WorkoutScreen.route) {
            holderViewModel.mainHeading.value = "Workouts"
            holderViewModel.concernedItem.value = 1
            Workouts(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.RemindersScreen.route) {
            holderViewModel.mainHeading.value = "Reminders"
            holderViewModel.concernedItem.value = 2
            Reminders(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.NutritionScreen.route) {
            holderViewModel.mainHeading.value = "Nutrition"
            holderViewModel.concernedItem.value = 3
            Nutrition(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.TimerScreen.route) {
            holderViewModel.mainHeading.value = "Timer"
            holderViewModel.concernedItem.value = 4
            Timer(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.BMIScreen.route) {
            holderViewModel.mainHeading.value = ""
            holderViewModel.concernedItem.value = 5
            Bmi(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.SettingsScreen.route) {
            holderViewModel.mainHeading.value = ""
            holderViewModel.concernedItem.value = 6
            Settings(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.AboutScreen.route) {
            holderViewModel.mainHeading.value = "About"
            holderViewModel.concernedItem.value = 7
            About(navControl, holderViewModel, drawerState, scope)
        }
        composable(route = ScreenConfiguration.LicensesScreen.route) {
            holderViewModel.mainHeading.value = "Licenses"
            holderViewModel.concernedItem.value = 8
            Licenses(navControl, holderViewModel, drawerState, scope)
        }

        //Secondary
        composable(route = ScreenConfiguration.CalendarView.route) {
            holderViewModel.fragHeading.value = "History"
            Calendar(navControl, holderViewModel)
        }
    }
}