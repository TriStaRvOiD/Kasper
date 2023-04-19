/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.domain

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.vanguard.data.repo.StatusDataStoreRepository
import com.tristarvoid.vanguard.data.sensor.MeasurableSensor
import com.tristarvoid.vanguard.data.steps.Steps
import com.tristarvoid.vanguard.data.steps.StepsDao
import com.tristarvoid.vanguard.data.steps.StepsEvent
import com.tristarvoid.vanguard.data.steps.StepsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val sensor: MeasurableSensor,
    private val dao: StepsDao,
    private val repository: StatusDataStoreRepository
) : ViewModel() {

    val isActive = mutableStateOf(false)

    private val _date = MutableStateFlow(LocalDate.now().toEpochDay())

    private val _state = MutableStateFlow(StepsState())
    val state = _state.asStateFlow()

    init {
        updateStatus()
        viewModelScope.launch {
            createEntryIfRequired()
            dao.getStepValues(_date.value).collect {
                onEvent(StepsEvent.SetCurrentSteps(it))
            }
            updateState()
        }
    }

    fun start() {
        sensor.startListening()
        viewModelScope.launch {
            repository.saveListeningState(true)
        }
        updateStatus()
        sensor.setOnSensorValuesChangedListener {
            val value = it[0].toInt()
            onEvent(StepsEvent.SetCurrentSteps(value))
            onEvent(StepsEvent.SetCalories(calculateCalories(value)))
            onEvent(StepsEvent.SaveEntry)
            updateState()
        }
    }

    fun stop() {
        sensor.stopListening()
        viewModelScope.launch {
            repository.saveListeningState(false)
        }
        updateStatus()
    }

    private fun updateState() {
        viewModelScope.launch {
            dao.getAvgStepValues().collect {
                onEvent(StepsEvent.SetAvgSteps(it))
            }
        }
    }

    //This method will change in the future.
    // Here weight is 70kg, height is 6 ft
    private fun calculateCalories(value: Int): Int {
        return ((0.57 * 70) + (0.415 * 6) + (0.00063 * value)).toInt()  // An unreliable formula that needs to be changed
    }

    private suspend fun createEntryIfRequired() {
        if (dao.entryPresence(_date.value) == 1)
            onEvent(StepsEvent.SaveEntry)
    }

    fun onEvent(
        event: StepsEvent
    ) {
        when (event) {
            //Update step count in state
            is StepsEvent.SetCurrentSteps -> {
                _state.update {
                    it.copy(
                        currentSteps = event.steps
                    )
                }
            }
            //Update goal in state
            is StepsEvent.SetGoal -> {
                _state.update {
                    it.copy(
                        goal = event.goal
                    )
                }
            }

            //Update calorie count in state
            is StepsEvent.SetCalories -> {
                _state.update {
                    it.copy(
                        calories = event.calories
                    )
                }
            }

            is StepsEvent.SetAvgSteps -> {
                _state.update {
                    it.copy(
                        avgSteps = event.avgSteps
                    )
                }
            }

            is StepsEvent.SaveEntry -> {
                val data = Steps(
                    currentSteps = _state.value.currentSteps,
                    goal = _state.value.goal,
                    calories = _state.value.calories,
                    id = _date.value
                )
                viewModelScope.launch {
                    dao.upsertEntry(data)
                }
            }

        }
    }

    private fun updateStatus() {
        viewModelScope.launch {
            repository.readListeningState().collect { status ->
                isActive.value = status
            }
        }
    }
}