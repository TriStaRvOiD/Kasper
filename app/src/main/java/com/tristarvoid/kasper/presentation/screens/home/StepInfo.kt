/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.ext.formatDecimalSeparator
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.view.CustomCard
import com.tristarvoid.kasper.view.LottieLoader

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