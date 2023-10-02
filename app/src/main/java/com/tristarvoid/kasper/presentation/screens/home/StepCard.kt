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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tristarvoid.kasper.presentation.components.CustomCard
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.utils.formatDecimalSeparator

@Composable
fun StepCard(
    steps: Int,
    goalInState: Int,
    averageSteps: Int,
    alterGoal: (Int) -> Unit
) {
    var goalEntry by remember {
        mutableIntStateOf(value = goalInState)
    }
    val openDialog = remember {
        mutableStateOf(value = false)
    }
    if (openDialog.value)
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "Enter goal",
                    fontFamily = JosefinSans
                )
            },
            text = {
                OutlinedTextField(
                    value = goalEntry.toString(),
                    onValueChange = {
                        if (it.length in 1..5) goalEntry = it.toInt()
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
                        alterGoal(goalEntry)
                    }
                ) {
                    Text(
                        text = "Ok",
                        fontFamily = JosefinSans
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        goalEntry = goalInState
                    }
                ) {
                    Text(
                        text = "Cancel",
                        fontFamily = JosefinSans
                    )
                }
            }
        )
    CustomCard(
        modifier = Modifier,
        onClick = {
            openDialog.value = true
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = steps.formatDecimalSeparator(),
                fontSize = 70.sp,
                fontFamily = JosefinSans,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.padding(top = 54.dp),
                text = "steps",
                fontSize = 20.sp,
                fontFamily = JosefinSans,
                maxLines = 1
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
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = JosefinSans,
                    maxLines = 1
                )
                Text(
                    text = "Steps Remaining: " + "${
                        if (goalInState > steps)
                            (goalInState - steps).formatDecimalSeparator()
                        else
                            0
                    } more",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = JosefinSans,
                    maxLines = 1
                )
                Text(
                    text = "Daily Average: ${averageSteps.formatDecimalSeparator()} steps",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = JosefinSans,
                    maxLines = 1
                )
                Text(
                    text = "Status: Idle",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = JosefinSans,
                    maxLines = 1
                )
            }
        }
    }
}