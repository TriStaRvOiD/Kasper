/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tristarvoid.kasper.location.presentation.view_models.LocationViewModel
import com.tristarvoid.kasper.map.presentation.components.MapCard

@Composable
fun MapScreen(
    isLocationPermissionGranted: Boolean,
    locationViewModel: LocationViewModel,
    handleLocationPermission: () -> Unit
) {
    val locationState by locationViewModel.locationState.collectAsState()
    val isLocationTrackingOn by locationViewModel.isLocationServiceActive.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        MapCard(
            modifier = Modifier.fillMaxSize(),
            isActive = isLocationTrackingOn,
            isLocationPermissionGranted = isLocationPermissionGranted,
            region = if (locationState.listOfRegions.isNotEmpty()) locationState.listOfRegions.last() else null,
            handleLocationPermission = handleLocationPermission
        )
    }
}