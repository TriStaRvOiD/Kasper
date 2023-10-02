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

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.utils.isDark

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onThemeToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(state = rememberScrollState(), enabled = true)
    ) {
        val permissionStatus =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION).status
            } else {
                TODO("VERSION.SDK_INT < Q")
            }
        val stepsViewModel: StepsViewModel = hiltViewModel()
        val stepsState by stepsViewModel.stepsState.collectAsState()
        val isActive by stepsViewModel.isTheSensorActive.collectAsState()
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "T o d a y",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.W400,
                fontFamily = JosefinSans
            )
            Icon(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                    onThemeToggle()
                },
                imageVector = if (MaterialTheme.colorScheme.isDark()) ImageVector.vectorResource(id = R.drawable.light_mode) else ImageVector.vectorResource(
                    id = R.drawable.dark_mode
                ),
                contentDescription = "Theme switch button"
            )
        }
        StepCard(
            steps = stepsState.currentSteps,
            goalInState = stepsState.goal,
            averageSteps = stepsState.averageSteps,
            alterGoal = {
                stepsViewModel.alterGoal(it)
            }
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
                CalorieCard(
                    modifier = Modifier.fillMaxWidth(),
                    calories = stepsState.caloriesBurned.toString()
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                ControlCard(
                    modifier = Modifier.fillMaxSize(),
                    active = isActive,
                    permissionStatus = permissionStatus
                )
            }
            Spacer(modifier = Modifier.width(width = 8.dp))
            Weather(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(weight = 0.55f)
            )
        }
        Spacer(modifier = Modifier.height(height = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }
}