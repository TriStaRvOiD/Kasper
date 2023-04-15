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

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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

    private val _theFucker = MutableStateFlow(0)

    private val _initialStart = MutableStateFlow(true)

    init {
        updateStatus()
        createEntryIfRequired()
        viewModelScope.launch {
            updatePastSteps()
        }
        updateState()
        Log.d("Steps", "${_theFucker.value}")
    }

    private fun createEntryIfRequired() {
        viewModelScope.launch {
            if (dao.entryPresence() == 1)
                onEvent(StepsEvent.SaveEntry)
        }
    }

    private fun updateState() {
        onEvent(StepsEvent.SetCurrentSteps(0))
        onEvent(StepsEvent.SetGoal(getGoal()))
        onEvent(StepsEvent.SetCalories(getCalories()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun updatePastSteps() {
        val job = viewModelScope.async {
            return@async dao.getStepValues(_date.value)
        }
        job.invokeOnCompletion {
            if (it == null) {
                _theFucker.value = job.getCompleted()
            }
        }
    }

    private fun getGoal(): Int {
        var current = 0
        viewModelScope.launch {
            current = dao.getGoalValue(_date.value)
        }
        return current
    }

    private fun getCalories(): Int {
        var current = 0
        viewModelScope.launch {
            current = dao.getCalorieValue(_date.value)
        }
        return current
    }

    fun onEvent(
        event: StepsEvent
    ) {
        when (event) {
            //Update step count in state
            is StepsEvent.SetCurrentSteps -> {
                _state.update {
                    it.copy(
                        currentSteps = event.steps + _theFucker.value
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

    fun start() {
        sensor.startListening()
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveListeningState(true)
        }
        updateStatus()
        sensor.setOnSensorValuesChangedListener { values ->
            if (_initialStart.value) {  //Initial app start, or after restart
                onEvent(
                    StepsEvent.SetCurrentSteps(
                        steps = values[0].toInt()
                    )
                )
                _initialStart.value = false
            } else {
                onEvent(
                    StepsEvent.SetCurrentSteps(
                        steps = values[0].toInt() - _theFucker.value
                    )
                )
            }
            onEvent(StepsEvent.SaveEntry)
        }
    }

    fun stop() {
        sensor.stopListening()
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveListeningState(false)
        }
        updateStatus()
    }
}