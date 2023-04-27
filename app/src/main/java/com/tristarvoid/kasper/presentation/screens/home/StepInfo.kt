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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.data.steps.StepsEvent
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.utils.formatDecimalSeparator
import com.tristarvoid.kasper.view.CustomCard
import com.tristarvoid.kasper.view.LottieLoader

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StepInfo(
    stepsViewModel: StepsViewModel,
    isActive: Boolean,
    permissionStatus: PermissionStatus
) {
    val state by stepsViewModel.state.collectAsState()
    val steps = state.currentSteps
    val goalInState = state.goal
    val goalChange = remember {
        mutableStateOf(goalInState)
    }
    val calories = state.calories
    val avgSteps = state.avgSteps
    val remainingSteps = state.remainingSteps
    if (isActive) {
        if (permissionStatus == PermissionStatus.Granted)
            stepsViewModel.start()
        else
            stepsViewModel.stop()
    }
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value)
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text("Enter goal")
            },
            text = {
                OutlinedTextField(
                    value = goalChange.value.toString(),
                    onValueChange = {
                        if (it.length in 1..5) goalChange.value = it.toInt()
                    },
                    label = { Text("Text") },
                    textStyle = TextStyle(fontFamily = JosefinSans, fontSize = 21.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        stepsViewModel.onEvent(StepsEvent.SetGoal(goalChange.value))
                        stepsViewModel.onEvent(StepsEvent.SaveEntry)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        goalChange.value = goalInState
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    CustomCard(
        modifier = Modifier
            .heightIn(min = 170.dp, max = 170.dp),
        function = {
            openDialog.value = true
        }
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
                    text = "Current goal: ${goalInState.formatDecimalSeparator()} steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Steps Remaining: " + if (remainingSteps == 0) "none" else "${remainingSteps.formatDecimalSeparator()} steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Daily Average: ${avgSteps.formatDecimalSeparator()} steps",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "Calories burned: ${calories.formatDecimalSeparator()} kcal",
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