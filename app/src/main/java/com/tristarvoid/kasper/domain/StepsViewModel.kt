/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.domain

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.data.repo.StatusDataStoreRepository
import com.tristarvoid.kasper.data.sensor.MeasurableSensor
import com.tristarvoid.kasper.data.steps.StepsDao
import com.tristarvoid.kasper.data.steps.StepsData
import com.tristarvoid.kasper.data.steps.StepsEvent
import com.tristarvoid.kasper.data.steps.StepsState
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
        updateState()
        updateRemainingSteps()
        updateAvgSteps()
        updateStateAlt()
    }

    fun start() {
        sensor.startListening()
        viewModelScope.launch {
            repository.saveListeningState(true)
        }
        updateStatus()
        sensor.setOnSensorValuesChangedListener {
            viewModelScope.launch {
                createBlankEntryIfRequired()
            }
            incrementSteps()
            onEvent(StepsEvent.SetCalories(calculateCalories()))
            onEvent(StepsEvent.SaveEntry)
        }
    }

    fun stop() {
        sensor.stopListening()
        viewModelScope.launch {
            repository.saveListeningState(false)
        }
        updateStatus()
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

            is StepsEvent.SetDataList -> {
                _state.update {
                    it.copy(
                        stepsData = event.listData
                    )
                }
            }

            is StepsEvent.SaveEntry -> {
                val data = StepsData(
                    currentSteps = _state.value.currentSteps,
                    goal = _state.value.goal,
                    calories = _state.value.calories,
                    id = _date.value
                )
                viewModelScope.launch {
                    dao.upsertEntry(data)
                }
            }

            is StepsEvent.SetRemainingSteps -> {
                _state.update {
                    it.copy(
                        remainingSteps = event.remainingSteps
                    )
                }
            }
        }
    }

    private fun updateState() {
        viewModelScope.launch {
            createBlankEntryIfRequired()
            dao.getEverything(_date.value).collect { stepsData ->
                onEvent(StepsEvent.SetCurrentSteps(stepsData.currentSteps))
                onEvent(StepsEvent.SetGoal(stepsData.goal))
                onEvent(StepsEvent.SetCalories(stepsData.calories))
            }
        }
    }

    private fun updateStateAlt() {
        viewModelScope.launch {
            dao.getEverythingAlt().collect { stepsData ->
                onEvent(StepsEvent.SetDataList(stepsData))
            }
        }
    }

    private fun incrementSteps() {
        onEvent(StepsEvent.SetCurrentSteps(_state.value.currentSteps + 1))
    }

    private fun updateAvgSteps() {
        viewModelScope.launch {
            dao.getAvgStepValue().collect {
                onEvent(StepsEvent.SetAvgSteps(it))
            }
        }
    }

    private fun updateRemainingSteps() {
        viewModelScope.launch {
            dao.getRemainingStepValue(_date.value).collect {
                if (it >= 0)
                    onEvent(StepsEvent.SetRemainingSteps(it))
            }
        }
    }

    //This method will change in the future.
    // Here weight is 70kg, height is 6 ft
    private fun calculateCalories(): Int {
        return ((0.57 * 70) + (0.415 * 6) + (0.00063 * _state.value.currentSteps)).toInt()  // An unreliable formula that needs to be changed
    }

    private suspend fun createBlankEntryIfRequired() {
        if (dao.entryPresence(_date.value) == 1) {
            onEvent(StepsEvent.SetCurrentSteps(0))
            onEvent(StepsEvent.SetCalories(0))
            onEvent(StepsEvent.SaveEntry)
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