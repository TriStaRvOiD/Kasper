/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.presentation.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.domain.ToastMaker
import com.tristarvoid.vanguard.domain.use_cases.GraphDataViewModel
import com.tristarvoid.vanguard.domain.use_cases.HolderViewModel
import com.tristarvoid.vanguard.domain.use_cases.StepsViewModel
import com.tristarvoid.vanguard.presentation.navigation.MainAppBar
import com.tristarvoid.vanguard.presentation.ui.theme.JosefinSans
import com.tristarvoid.vanguard.util.Header
import com.tristarvoid.vanguard.util.LottieLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope,
    toaster: ToastMaker = hiltViewModel()
) {
    var showToast by remember { mutableStateOf(false) }

    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }

    if (showToast) {
        toaster.displayToast("Press back again to exit", Toast.LENGTH_LONG)
        showToast = false
    }

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }

    Scaffold(
        topBar = {
            MainAppBar(navControl, drawerState, scope, holderViewModel)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            val permissionState =
                rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
            if (permissionState.status == PermissionStatus.Granted) {
                val viewModel: StepsViewModel = hiltViewModel()
                val steps by viewModel.steps.collectAsState()
                Column(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding())
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Header(
                        modifier = Modifier
                            .fillMaxWidth()
                            .paddingFromBaseline(5.dp),
                        alignment = Alignment.TopStart,
                        text = "Today"
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Divider() //Below header
                    Spacer(modifier = Modifier.height(16.dp))
                    StepCard(steps)
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Weather()
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Quote()
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    CalorieGraph()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun StepCard(
    steps: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 190.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = LocalContentColor.current
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = (if (steps == "") "~" else steps),
                fontSize = 50.sp,
                fontFamily = JosefinSans
            )
            Text(
                text = "steps",
                fontSize = 15.sp,
                fontFamily = JosefinSans
            )
            Spacer(modifier = Modifier.height(17.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 26.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Current goal: 10,000 steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Steps Remaining: 2000 steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Daily Average: 4000 steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Calories burned: 1500 kcal",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Status: Enabled",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
            }
            LottieLoader(
                jsonResource = R.raw.heartbeat,
                size = 70
            )
        }
    }
}

@Composable
fun Weather() {
    Text(
        text = "Seattle, WA : ",
        style = MaterialTheme.typography.titleMedium,
        fontFamily = JosefinSans
    )
    Text(
        text = "It's 20 degrees, with light rain",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .padding(start = 100.dp),
        fontFamily = JosefinSans
    )
}

@Composable
fun Quote() {
    Text(
        text = "Daily Quote : ",
        style = MaterialTheme.typography.titleMedium,
        fontFamily = JosefinSans
    )
    Text(
        text = "Walking is good. Keep walking.",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .padding(start = 100.dp),
        fontFamily = JosefinSans
    )
}

@Composable
fun CalorieGraph() {
    val calorieViewModel = viewModel<GraphDataViewModel>()
    Text(
        text = "Activity : ",
        style = MaterialTheme.typography.titleMedium,
        fontFamily = JosefinSans
    )
    Spacer(modifier = Modifier.height(5.dp))
    Chart(
        chart = columnChart(),
        chartModelProducer = calorieViewModel.chartEntryModelProducer,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(),
    )
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}
