/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.screens.home

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.domain.QuoteViewModel
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.domain.ToastMaker
import com.tristarvoid.kasper.domain.WeatherViewModel
import com.tristarvoid.kasper.presentation.navigation.MainAppBar
import com.tristarvoid.kasper.view.Header
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION).status
            } else {
                TODO("VERSION.SDK_INT < Q")
            }
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
            weatherViewModel.getQuality(19.0760, 72.8777)
            holderViewModel.apisCalled.value = true
        }
        val quote by quoteViewModel.quote.collectAsState()
        val temp = remember {
            weatherViewModel.temp
        }
        val desc = remember {
            weatherViewModel.desc
        }
        val quality = remember {
            weatherViewModel.quality
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
                text = if (timeOfDay < 12) "It's mornin'" else if (timeOfDay < 16) "It's afternoon" else "It's evenin'",
                fontSize = 35
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
                Weather(desc.value, temp.value, quality.value)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CalorieGraph()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}