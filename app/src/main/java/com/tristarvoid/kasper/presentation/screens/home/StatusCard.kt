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

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.view.CustomCard

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