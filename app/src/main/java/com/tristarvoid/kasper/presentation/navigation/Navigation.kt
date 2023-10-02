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

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tristarvoid.kasper.domain.NavigationViewModel
import com.tristarvoid.kasper.domain.ThemeViewModel
import com.tristarvoid.kasper.domain.ToastViewModel
import com.tristarvoid.kasper.presentation.screens.home.HomeScreen
import com.tristarvoid.kasper.presentation.screens.more.About
import com.tristarvoid.kasper.presentation.screens.more.Licenses
import com.tristarvoid.kasper.presentation.screens.more.MoreScreen
import com.tristarvoid.kasper.presentation.screens.more.Settings
import com.tristarvoid.kasper.presentation.screens.summary.Calendar
import kotlinx.coroutines.delay

@Composable
fun Navigation(
    navigationViewModel: NavigationViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    defaultScreen: String
) {
    NavHost(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
        ),
        navController = navController,
        startDestination = defaultScreen
    ) {
        homeRoute(
            onThemeToggle = {
                themeViewModel.isDarkTheme.value = !themeViewModel.isDarkTheme.value
            },
            onBackPress = {
                val toaster: ToastViewModel = hiltViewModel()
                var showToast by remember { mutableStateOf(value = false) }
                var backPressState by remember { mutableStateOf<BackPress>(value = BackPress.Idle) }

                if (showToast) {
                    toaster.displayToast(
                        message = "Press back again to exit",
                        length = Toast.LENGTH_LONG
                    )
                    showToast = false
                }

                LaunchedEffect(key1 = backPressState) {
                    if (backPressState == BackPress.InitialTouch) {
                        delay(timeMillis = 2000)
                        backPressState = BackPress.Idle
                    }
                }

                BackHandler(backPressState == BackPress.Idle) {
                    backPressState = BackPress.InitialTouch
                    showToast = true
                }

            }
        )

        summaryRoute(
            title = "Summary",
            onBackPress = {
                BackHandler(enabled = true) {
                    navController.popBackStack()
                    navigationViewModel.selectedItemIndex.value = 0
                }
            }
        )

        moreRoute(
            title = "More",
            onBackPress = {
                BackHandler(enabled = true) {
                    navController.popBackStack()
                    navigationViewModel.selectedItemIndex.value = 0
                }
            }
        )

        settingsRoute(
            title = "Settings",
            viewModel = themeViewModel
        )

        aboutRoute(
            title = "About"
        )

        licensesRoute(
            title = "Licenses"
        )
    }
}

fun NavGraphBuilder.homeRoute(
    onThemeToggle: () -> Unit,
    onBackPress: @Composable () -> Unit
) {
    composable(route = ScreenConfiguration.HomeScreen.route) {
        onBackPress()
        HomeScreen(
            onThemeToggle = onThemeToggle
        )
    }
}

fun NavGraphBuilder.summaryRoute(
    title: String,
    onBackPress: @Composable () -> Unit
) {
    composable(route = ScreenConfiguration.SummaryScreen.route) {
        onBackPress()
        Calendar(title)
    }
}

fun NavGraphBuilder.moreRoute(
    title: String,
    onBackPress: @Composable () -> Unit
) {
    composable(route = ScreenConfiguration.MoreScreen.route) {
        onBackPress()
        MoreScreen(
            title = title
        )
    }
}

fun NavGraphBuilder.settingsRoute(
    title: String,
    viewModel: ThemeViewModel
) {
    composable(route = ScreenConfiguration.SettingsScreen.route) {
        val dynamicIsEnabled = viewModel.dynamicEnabled.collectAsStateWithLifecycle()
        Settings(
            title = title,
            text = if (dynamicIsEnabled.value) "Click to disable dynamic theming" else "Click to enable dynamic theming",
            onClick = {
                viewModel.dynamicEnabled.value = !dynamicIsEnabled.value
            }
        )
    }
}

fun NavGraphBuilder.aboutRoute(
    title: String
) {
    composable(route = ScreenConfiguration.AboutScreen.route) {
        About(
            title = title
        )
    }
}

fun NavGraphBuilder.licensesRoute(
    title: String
) {
    composable(route = ScreenConfiguration.LicensesScreen.route) {
        Licenses(
            title = title
        )
    }
}

sealed class BackPress {
    data object Idle : BackPress()
    data object InitialTouch : BackPress()
}