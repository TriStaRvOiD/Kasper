/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.domain.SplashScreenViewModel
import com.tristarvoid.kasper.presentation.navigation.DrawerBody
import com.tristarvoid.kasper.presentation.navigation.Navigation
import com.tristarvoid.kasper.presentation.navigation.ScreenConfiguration
import com.tristarvoid.kasper.presentation.ui.theme.VanguardTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var splashViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }
        setContent {
            val holderViewModel = viewModel<HolderViewModel>()
            val systemUiController = rememberSystemUiController()
            val dynamicEnabled = holderViewModel.dynamicEnabled.collectAsStateWithLifecycle()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }
            VanguardTheme(
                dynamicColor = dynamicEnabled.value
            ) {
                val screen by splashViewModel.startDestination
                Surface {
                    Display(holderViewModel, screen)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Display(
    holderViewModel: HolderViewModel,
    screen: String
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerBody(
                navControl = navController,
                drawerState = drawerState,
                scope = scope,
                holderViewModel = holderViewModel,
                items = listOf(
                    MenuItem(
                        id = ScreenConfiguration.HomeScreen.route,
                        title = "Home",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.home)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.WorkoutScreen.route,
                        title = "Workouts",
                        contentDescription = "Go to workouts screen",
                        icon = painterResource(id = R.drawable.exercise)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.RemindersScreen.route,
                        title = "Reminders",
                        contentDescription = "Go to reminders screen",
                        icon = painterResource(id = R.drawable.reminders)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.NutritionScreen.route,
                        title = "Nutrition",
                        contentDescription = "Go to nutrition screen",
                        icon = painterResource(id = R.drawable.nutrition)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.TimerScreen.route,
                        title = "Timer",
                        contentDescription = "Go to timer screen",
                        icon = painterResource(id = R.drawable.timer)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.BMIScreen.route,
                        title = "BMI",
                        contentDescription = "Go to privacy screen",
                        icon = painterResource(id = R.drawable.bmi)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.SettingsScreen.route,
                        title = "Settings",
                        contentDescription = "Go to settings screen",
                        icon = painterResource(id = R.drawable.settings)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.AboutScreen.route,
                        title = "About",
                        contentDescription = "Go to about screen",
                        icon = painterResource(id = R.drawable.info)
                    ),
                    MenuItem(
                        id = ScreenConfiguration.LicensesScreen.route,
                        title = "Licenses",
                        contentDescription = "Go to water screen",
                        icon = painterResource(id = R.drawable.licenses)
                    )
                )
            )
        }
    ) {
        Navigation(navController, holderViewModel, drawerState, scope, screen)
    }
}

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: Painter
)