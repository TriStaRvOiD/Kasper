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

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.tristarvoid.kasper.presentation.components.CustomCard
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.service.StepDetectorService
import com.tristarvoid.kasper.utils.isDark

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ControlCard(
    modifier: Modifier = Modifier,
    active: Boolean,
    permissionStatus: PermissionStatus,
    localContext: Context = LocalContext.current
) {
    val stepsServiceIntent = Intent(localContext, StepDetectorService::class.java)
    CustomCard(
        enableGlow = active && MaterialTheme.colorScheme.isDark(),
        modifier = modifier,
        onClick = {
            if (permissionStatus == PermissionStatus.Granted) {
                if (active)
                    localContext.stopService(stepsServiceIntent)
                else
                    localContext.startForegroundService(stepsServiceIntent)
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Service is " + if (active) "enabled" else "paused",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = JosefinSans
            )
        }
    }
}