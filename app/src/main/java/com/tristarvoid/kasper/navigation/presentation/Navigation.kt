/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.navigation.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.about.presentation.About
import com.tristarvoid.kasper.core.presentation.components.TypewriterText
import com.tristarvoid.kasper.core.presentation.utils.ScreenConfiguration
import com.tristarvoid.kasper.core.presentation.utils.isDark
import com.tristarvoid.kasper.core.presentation.screens.HomeScreen
import com.tristarvoid.kasper.location.presentation.view_models.LocationViewModel
import com.tristarvoid.kasper.map.presentation.MapScreen
import com.tristarvoid.kasper.permissions.presentation.view_models.PermissionsViewModel
import com.tristarvoid.kasper.core.presentation.screens.Settings
import com.tristarvoid.kasper.steps.presentation.view_models.StepsViewModel
import com.tristarvoid.kasper.summary.presentation.Calendar
import com.tristarvoid.kasper.theme.presentation.view_models.ThemeViewModel
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Navigation(
    paddingValues: PaddingValues,
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    defaultScreen: String,
) {
    val context = LocalContext.current
    val currentDate = remember { DateFormat.getDateInstance().format(Date()) }
    val timeOfDayText by rememberUpdatedState(getTextForTimeOfDay())
    val permissionsViewModel: PermissionsViewModel = hiltViewModel()
    val stepsViewModel: StepsViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
    val activityPermissionCount by permissionsViewModel.activityRequestCount.collectAsStateWithLifecycle()
    val locationPermissionCount by permissionsViewModel.locationRequestCount.collectAsStateWithLifecycle()
    val activityPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TypewriterText(
                modifier = Modifier.padding(top = 8.dp),
                parts = listOf(timeOfDayText, "It's $currentDate")
            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                IconButton(onClick = {
                    themeViewModel.toggleThemeMode()
//                    themeViewModel.toggleDynamicPref()
                }) {
                    Icon(
                        imageVector = if (MaterialTheme.colorScheme.isDark())
                            ImageVector.vectorResource(id = R.drawable.light_mode)
                        else ImageVector.vectorResource(id = R.drawable.dark_mode),
                        contentDescription = "Theme switch button"
                    )
                }
            }
        }
        NavHost(
            navController = navController,
            startDestination = defaultScreen
        ) {
            homeRoute(
                stepsViewModel = stepsViewModel,
                isActivityPermissionGranted = activityPermissionState.status.isGranted,
                locationViewModel = locationViewModel,
                isLocationPermissionGranted = locationPermissionState.status.isGranted,
                onBackPress = {
                    var showToast by remember { mutableStateOf(value = false) }
                    var backPressState by remember { mutableStateOf<BackPress>(value = BackPress.Idle) }

                    if (showToast) {
                        Toast.makeText(
                            LocalContext.current, "Press back again to exit", Toast.LENGTH_LONG
                        ).show()
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
                },
                handleActivityPermission = {
                    if (activityPermissionCount < 2) {
                        activityPermissionState.launchPermissionRequest()
                        permissionsViewModel.incrementActivityRequestCount()
                    } else {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", context.packageName, null)
                        context.startActivity(intent)
                    }
                }
            )

            summaryRoute(stepsViewModel = stepsViewModel)

            mapRoute(
                locationViewModel = locationViewModel,
                isLocationPermissionGranted = locationPermissionState.status.isGranted,
                handleLocationPermission = {
                    if (locationPermissionCount < 2) {
                        locationPermissionState.launchPermissionRequest()
                        permissionsViewModel.incrementLocationRequestCount()
                    } else {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", context.packageName, null)
                        context.startActivity(intent)
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
        }
    }
}

fun NavGraphBuilder.homeRoute(
    stepsViewModel: StepsViewModel,
    isActivityPermissionGranted: Boolean,
    locationViewModel: LocationViewModel,
    isLocationPermissionGranted: Boolean,
    onBackPress: @Composable () -> Unit,
    handleActivityPermission: () -> Unit
) {
    composable(route = ScreenConfiguration.HomeScreen.route) {
        onBackPress()
        HomeScreen(
            stepsViewModel = stepsViewModel,
            isActivityPermissionGranted = isActivityPermissionGranted,
            locationViewModel = locationViewModel,
            isLocationPermissionGranted = isLocationPermissionGranted,
            handleActivityPermission = handleActivityPermission
        )
    }
}

fun NavGraphBuilder.summaryRoute(stepsViewModel: StepsViewModel) {
    composable(route = ScreenConfiguration.SummaryScreen.route) {
        Calendar(stepsViewModel = stepsViewModel)
    }
}

fun NavGraphBuilder.mapRoute(
    locationViewModel: LocationViewModel,
    isLocationPermissionGranted: Boolean,
    handleLocationPermission: () -> Unit
) {
    composable(route = ScreenConfiguration.MapScreen.route) {
        MapScreen(
            locationViewModel = locationViewModel,
            isLocationPermissionGranted = isLocationPermissionGranted,
            handleLocationPermission = handleLocationPermission
        )
    }
}

fun NavGraphBuilder.settingsRoute(
    title: String,
    viewModel: ThemeViewModel
) {
    composable(route = ScreenConfiguration.SettingsScreen.route) {
        val dynamicEnabled = viewModel.dynamicEnabled.collectAsStateWithLifecycle()
        Settings(
            title = title,
            text = if (dynamicEnabled.value == true) "Click to disable dynamic theming" else "Click to enable dynamic theming",
            onClick = {
                TODO()
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

sealed class BackPress {
    data object Idle : BackPress()
    data object InitialTouch : BackPress()
}

private fun getTextForTimeOfDay(): String {
    val currentTime = LocalTime.now()
    return when (currentTime.hour) {
        in 6..11 -> "Hey, good morning!"
        in 12..17 -> "Hey, good afternoon"
        in 18..20 -> "Hey, good evening"
        else -> "Hey... sleep, maybe?"
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}