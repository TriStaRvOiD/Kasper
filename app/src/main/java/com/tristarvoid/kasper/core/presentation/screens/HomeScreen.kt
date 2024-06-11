/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tristarvoid.kasper.io.presentation.components.IndoorsTimeCard
import com.tristarvoid.kasper.io.presentation.components.OutdoorsTimeCard
import com.tristarvoid.kasper.steps.presentation.components.StepCard
import com.tristarvoid.kasper.weather.presentation.components.Weather
import com.tristarvoid.kasper.steps.presentation.components.WeeklyChart
import com.tristarvoid.kasper.location.presentation.view_models.LocationViewModel
import com.tristarvoid.kasper.steps.presentation.view_models.StepsViewModel

@Composable
fun HomeScreen(
    stepsViewModel: StepsViewModel,
    isActivityPermissionGranted: Boolean,
    locationViewModel: LocationViewModel,
    isLocationPermissionGranted: Boolean,
    handleActivityPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(
                state = rememberScrollState(),
            )
    ) {
        val stepsState by stepsViewModel.stepsState.collectAsStateWithLifecycle()
        val isStepCountingActive by stepsViewModel.isStepServiceActive.collectAsStateWithLifecycle()
        val indoorsDuration by locationViewModel.indoorsDuration.collectAsStateWithLifecycle()
        val outdoorsDuration by locationViewModel.outdoorsDuration.collectAsStateWithLifecycle()
        StepCard(
            steps = stepsState.currentSteps,
            goalInState = stepsState.goal,
            averageSteps = stepsState.averageSteps,
            calories = stepsState.caloriesBurned.toString(),
            isActive = isStepCountingActive,
            isActivityPermissionGranted = isActivityPermissionGranted,
            alterGoal = {
                stepsViewModel.alterGoal(it)
            },
            handleActivityPermission = handleActivityPermission
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.45f)
                    .height(IntrinsicSize.Min)
            ) {
                IndoorsTimeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    indoorsDuration
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                OutdoorsTimeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    outdoorsDuration
                )
            }
            Spacer(modifier = Modifier.width(width = 8.dp))
            Weather(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(weight = 0.55f)
            )
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        WeeklyChart(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1.4f),
            stepsState = stepsState
        )
    }
}