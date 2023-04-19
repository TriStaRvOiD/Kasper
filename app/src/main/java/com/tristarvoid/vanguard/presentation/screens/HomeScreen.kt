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
import com.tristarvoid.vanguard.domain.*
import com.tristarvoid.vanguard.presentation.navigation.MainAppBar
import com.tristarvoid.vanguard.presentation.ui.theme.JosefinSans
import com.tristarvoid.vanguard.presentation.util.CustomCard
import com.tristarvoid.vanguard.presentation.util.Header
import com.tristarvoid.vanguard.presentation.util.LottieLoader
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
        val permissionStatus =
            rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION).status
        val stepsViewModel: StepsViewModel = hiltViewModel()
        val quoteViewModel: QuoteViewModel = hiltViewModel()
        val weatherViewModel: WeatherViewModel = hiltViewModel()
        val timeOfDay by remember {
            holderViewModel.timeOfDay
        }
        LaunchedEffect(Unit) {
            while (true) {
                delay(5000)
                holderViewModel.updateTime()
            }
        }
        //Remember whether apis have been called
        val apisCalled = remember {
            holderViewModel.apisCalled
        }
        //To prevent continuous network call
        if (!apisCalled.value) {
            quoteViewModel.getTheQuote()
            weatherViewModel.getWeather(19.0760, 72.8777)
            holderViewModel.apisCalled.value = true
        }
        //The quote
        val quote by quoteViewModel.quote.collectAsState()
        val temp = remember{
            weatherViewModel.temp
        }
        val desc = remember{
            weatherViewModel.desc
        }
        val isActive = remember {
            stepsViewModel.isActive
        }
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
                text = if (timeOfDay < 12) "It's mornin'" else if (timeOfDay < 16) "It's afternoon" else "It's evenin'"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider() //Below header
            Spacer(modifier = Modifier.height(16.dp))
            StepInfo(stepsViewModel, isActive.value, permissionStatus)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Quote(quote)
                    Spacer(modifier = Modifier.height(9.dp))
                    Control(isActive.value, stepsViewModel, permissionStatus)
                }
                Weather(desc.value, temp.value)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CalorieGraph()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StepInfo(
    stepsViewModel: StepsViewModel,
    isActive: Boolean,
    permissionStatus: PermissionStatus
) {
    val state by stepsViewModel.state.collectAsState()
    val steps = state.currentSteps
    val goal = state.goal
    val calories = state.calories
    val avgSteps = state.avgSteps
    val remainingSteps = remember {
        if (goal > steps) {
            mutableStateOf(goal - steps)
        } else
            mutableStateOf(0)
    }
    if (isActive) {
        if (permissionStatus == PermissionStatus.Granted)
            stepsViewModel.start()
        else
            stepsViewModel.stop()
    }
    CustomCard(
        modifier = Modifier
            .heightIn(min = 170.dp, max = 170.dp),
        function = {}
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = (if (steps > 100000) "100,000+" else (steps
                    .formatDecimalSeparator())),
                fontSize = 70.sp,
                fontFamily = JosefinSans
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .padding(top = 55.dp),
                text = "steps",
                fontSize = 20.sp,
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
                    text = "Current goal: $goal steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Steps Remaining: " + if (remainingSteps.value == 0) "none" else "$remainingSteps steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Daily Average: $avgSteps steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Calories burned: $calories kcal",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
            }
            if (isActive)
                LottieLoader(
                    jsonResource = R.raw.heartbeat,
                    size = 70
                )
        }
    }
}

@Composable
fun Weather(
    desc: String,
    temp: String
) {
    CustomCard(
        modifier = Modifier
            .widthIn(min = 178.dp, max = 178.dp)
            .heightIn(min = 145.dp, max = 145.dp),
        function = {}
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = "Mumbai, India : ",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = JosefinSans
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = "It's $temp degrees, with ${desc.lowercase()}",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = JosefinSans
        )
    }
}

@Composable
fun Quote(
    quote: String
) {
    CustomCard(
        modifier = Modifier
            .widthIn(min = 143.dp, max = 143.dp)
            .heightIn(min = 90.dp, max = 90.dp),
        function = {}
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = "Quote : ",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = JosefinSans
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = if (quote == "") "Loading . . ." else quote,
            style = MaterialTheme.typography.titleSmall,
            fontFamily = JosefinSans
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Control(
    active: Boolean,
    viewModel: StepsViewModel,
    permissionStatus: PermissionStatus
) {
    CustomCard(
        modifier = Modifier
            .widthIn(min = 143.dp, max = 143.dp)
            .heightIn(min = 46.dp, max = 46.dp),
        function = {
            if (permissionStatus == PermissionStatus.Granted) {
                if (active)
                    viewModel.stop()
                else
                    viewModel.start()
            }
        }
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = if (active) "Status: Enabled" else "Status: Paused",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = JosefinSans
        )
    }
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

//Puts a comma after every 3 digits
fun Int.formatDecimalSeparator(): String {
    return toString().reversed().chunked(3).joinToString(",").reversed()
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}
