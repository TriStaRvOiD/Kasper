/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.permissions.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.permissions.data.ActivityPermissionsDataStoreRepository
import com.tristarvoid.kasper.permissions.data.LocationPermissionDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val activityRepository: ActivityPermissionsDataStoreRepository,
    private val locationRepository: LocationPermissionDataStoreRepository
) : ViewModel() {

    private val _activityRequestCount = MutableStateFlow(0)
    val activityRequestCount = _activityRequestCount.asStateFlow()

    private val _locationRequestCount = MutableStateFlow(0)
    val locationRequestCount = _activityRequestCount.asStateFlow()

    init {
        syncActivityCount()
        syncLocationCount()
    }

    private fun syncActivityCount() {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.readRequestCount().collect { count ->
                _activityRequestCount.value = count
            }
        }
    }

    fun incrementActivityRequestCount() {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.saveRequestCount(_activityRequestCount.value + 1)
        }
    }

    private fun syncLocationCount() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.readRequestCount().collect { count ->
                _locationRequestCount.value = count
            }
        }
    }

    fun incrementLocationRequestCount() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.saveRequestCount(_locationRequestCount.value + 1)
        }
    }
}