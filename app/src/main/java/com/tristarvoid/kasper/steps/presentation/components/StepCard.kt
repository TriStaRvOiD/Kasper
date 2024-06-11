/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.presentation.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.core.presentation.components.CustomCard
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import com.tristarvoid.kasper.core.presentation.utils.formatDecimalSeparator
import com.tristarvoid.kasper.core.presentation.utils.isDark
import com.tristarvoid.kasper.steps.services.StepDetectorService
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StepCard(
    steps: Int,
    goalInState: Int,
    averageSteps: Int,
    calories: String,
    isActive: Boolean,
    isActivityPermissionGranted: Boolean,
    context: Context = LocalContext.current,
    alterGoal: (Int) -> Unit,
    handleActivityPermission: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val stepsServiceIntent = Intent(context, StepDetectorService::class.java)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val aspectRatio = if (screenWidth > screenHeight) 5.2f else 2.05f
    var goalEntry by remember {
        mutableStateOf(value = goalInState.toString())
    }
    val pagerState = rememberPagerState(pageCount = {
        2
    })
    LaunchedEffect(goalInState) {
        goalEntry = goalInState.toString()
    }
    CustomCard(
        enableGlow = MaterialTheme.colorScheme.isDark(),
//        color1 = Color(0xFFE0115F),
//        color2 = Color(0xFF009494),
        color1 = Color(0xFF8B008B),
        color2 = Color(0xFFFCD209),
//        color2 = Color(0xFF009494),
        elevation = 30,
        modifier = Modifier.aspectRatio(aspectRatio)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val isCurrent = pagerState.currentPage == iteration
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isCurrent) if (MaterialTheme.colorScheme.isDark()) Color.LightGray else Color.DarkGray else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCurrent) Color.LightGray else if (MaterialTheme.colorScheme.isDark()) Color.Gray else Color.DarkGray,
                            shape = CircleShape
                        )
                        .size(6.dp)
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = steps.formatDecimalSeparator(),
                                style = MaterialTheme.typography.displayMedium,
                                fontFamily = JosefinSans,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                modifier = Modifier.padding(top = 14.dp),
                                text = "steps",
                                style = MaterialTheme.typography.titleLarge,
                                fontFamily = JosefinSans,
                                maxLines = 1
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(1)
                                        }
                                    },
                                    text = "Current goal: ${goalInState.formatDecimalSeparator()} steps",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = JosefinSans,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Steps remaining: " + "${
                                        if (goalInState > steps)
                                            (goalInState - steps).formatDecimalSeparator()
                                        else
                                            0
                                    } more",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = JosefinSans,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Daily average: ${averageSteps.formatDecimalSeparator()} steps",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = JosefinSans,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Calories burned: $calories kcal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = JosefinSans,
                                    maxLines = 1
                                )
                            }
                            IconButton(modifier = Modifier.padding(top = 42.dp), onClick = {
                                if (isActivityPermissionGranted) {
                                    if (isActive)
                                        stepsServiceIntent.apply {
                                            action =
                                                StepDetectorService.ACTION_STEP_TRACKING_DISABLE
                                            context.startService(this)
                                        }
                                    else
                                        stepsServiceIntent.apply {
                                            action = StepDetectorService.ACTION_STEP_TRACKING_ENABLE
                                            context.startService(this)
                                        }
                                } else
                                    handleActivityPermission()
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = if (!isActive) R.drawable.play_outline_svgrepo_com else R.drawable.pause_outline_svgrepo_com),
                                    contentDescription = "Step tracking toggle button"
                                )
                            }
                        }
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 15.dp, top = 5.dp),
                    ) {
                        var isEditing by remember {
                            mutableStateOf(true)
                        }
                        Text(
                            text = if (goalInState == 0) "Set your goal here." else "Your goal for the day.",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = JosefinSans,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        AnimatedVisibility(visible = isEditing) {
                            OutlinedTextField(
                                value = goalEntry,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 5) {
                                        goalEntry = newValue.replace(Regex("[^0-9]"), "")
                                    }
                                },
                                textStyle = TextStyle(
                                    fontFamily = JosefinSans,
                                    fontSize = 20.sp
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    if (goalEntry == "")
                                        goalEntry = "0"
                                    if (goalEntry != goalInState.toString())
                                        isEditing = false
                                }),
                                singleLine = true
                            )
                        }
                        AnimatedVisibility(visible = !isEditing) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Current goal : ${goalInState.formatDecimalSeparator()}",
                                    fontFamily = JosefinSans,
                                )
                                Text(
                                    text = "New goal : ${
                                        goalEntry.toInt().formatDecimalSeparator()
                                    }", fontFamily = JosefinSans
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        modifier = Modifier.padding(top = 11.dp),
                                        text = "Confirm: ",
                                        fontFamily = JosefinSans
                                    )
                                    TextButton(
                                        onClick = {
                                            goalEntry = goalInState.toString()
                                            isEditing = true
                                        }
                                    ) {
                                        Text("No", fontFamily = JosefinSans)
                                    }

                                    TextButton(
                                        onClick = {
                                            alterGoal(goalEntry.toInt())
                                            isEditing = true
                                        }
                                    ) {
                                        Text("Yes", fontFamily = JosefinSans)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}