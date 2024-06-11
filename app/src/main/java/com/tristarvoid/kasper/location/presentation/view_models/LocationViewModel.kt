/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.location.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.location.data.LocationServiceStatusDataStoreRepository
import com.tristarvoid.kasper.io.data.IODao
import com.tristarvoid.kasper.io.presentation.IOEvent
import com.tristarvoid.kasper.io.presentation.IOState
import com.tristarvoid.kasper.location.data.LocationDao
import com.tristarvoid.kasper.location.presentation.LocationEvent
import com.tristarvoid.kasper.location.presentation.LocationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationDao: LocationDao,
    private val ioDao: IODao,
    private val statusRepository: LocationServiceStatusDataStoreRepository
) : ViewModel() {

    private val _locationState = MutableStateFlow(LocationState())
    val locationState = _locationState.asStateFlow()

    private val _ioState = MutableStateFlow(IOState())

    private val _indoorsDuration = MutableStateFlow(Duration.ZERO)
    val indoorsDuration = _indoorsDuration.asStateFlow()

    private val _outdoorsDuration = MutableStateFlow(Duration.ZERO)
    val outdoorsDuration = _outdoorsDuration.asStateFlow()

    private val _isServiceActive = MutableStateFlow(value = false)
    val isLocationServiceActive = _isServiceActive.asStateFlow()

    init {
        keepStateUpToDate()
        keepIOStateUpToDate()
        keepServiceStatusUpToDate()
        keepDurationsUpToDate()
    }

    private fun onEvent(locationEvent: LocationEvent) {
        when (locationEvent) {
            is LocationEvent.SetListInState -> {
                _locationState.update { state ->
                    state.copy(
                        listOfRegions = locationEvent.regions
                    )
                }
            }
        }
    }

    private fun onIOEvent(ioEvent: IOEvent) {
        when (ioEvent) {
            is IOEvent.SetUserIOListForDayInState -> {
                _ioState.update { state ->
                    state.copy(
                        ioStateListForDay = ioEvent.ioStateList
                    )
                }
            }

            is IOEvent.SetUserIOListInState -> TODO()
        }
    }

    private fun keepDurationsUpToDate() {
        viewModelScope.launch {
            _ioState.collect { state ->
                val list = state.ioStateListForDay
                if (list.isNotEmpty()) {
                    var lastIsIndoors: Boolean? = null
                    var lastTimestamp = 0L
                    var indoorsDuration = Duration.ZERO
                    var outdoorsDuration = Duration.ZERO

                    for (ioState in list) {
                        val currentIsIndoors = ioState.isIndoors
                        val currentTimestamp = ioState.dayNanos

                        if (lastIsIndoors != null) {
                            val duration = Duration.ofNanos(currentTimestamp - lastTimestamp)
                            if (lastIsIndoors == true)
                                indoorsDuration += duration
                            else
                                outdoorsDuration += duration
                        }

                        lastIsIndoors = currentIsIndoors
                        lastTimestamp = currentTimestamp
                    }

                    _indoorsDuration.value = indoorsDuration
                    _outdoorsDuration.value = outdoorsDuration
                }
            }
        }
    }

    private fun keepStateUpToDate() {
        viewModelScope.launch(Dispatchers.IO) {
            locationDao.getRegionList(LocalDate.now().toEpochDay()).collect { list ->
                onEvent(LocationEvent.SetListInState(list))
            }
        }
    }

    private fun keepIOStateUpToDate() {
        viewModelScope.launch {
            ioDao.getIOList(LocalDate.now().toEpochDay()).collect { list ->
                if (list.isNotEmpty()) {
                    onIOEvent(IOEvent.SetUserIOListForDayInState(list))
                }
            }
        }
    }

    private fun keepServiceStatusUpToDate() {
        viewModelScope.launch(context = Dispatchers.IO) {
            statusRepository.readListeningState().collect { status ->
                _isServiceActive.value = status
            }
        }
    }
}