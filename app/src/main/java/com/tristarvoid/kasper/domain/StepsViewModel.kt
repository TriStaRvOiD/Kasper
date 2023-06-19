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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.data.datastore.GoalDataStoreRepository
import com.tristarvoid.kasper.data.datastore.StatusDataStoreRepository
import com.tristarvoid.kasper.data.steps.StepsDao
import com.tristarvoid.kasper.data.steps.StepsEvent
import com.tristarvoid.kasper.data.steps.StepsState
import com.tristarvoid.kasper.utils.calculateCalorieValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val stepsDao: StepsDao,
    private val statusRepository: StatusDataStoreRepository,
    private val goalRepository: GoalDataStoreRepository
) : ViewModel() {

    private val _stepsState = MutableStateFlow(value = StepsState())
    val stepsState = _stepsState.asStateFlow()

    private val _isTheSensorActive = MutableStateFlow(value = false)
    val isTheSensorActive = _isTheSensorActive.asStateFlow()

    private val _date = MutableStateFlow(value = LocalDate.now().toEpochDay())

    init {
        keepSensorStatusUpToDate()
        keepStateUpToDate()
    }

    private fun onEvent(
        event: StepsEvent
    ) {
        when (event) {
            is StepsEvent.SetCurrentStepCountInState -> {
                _stepsState.update { state ->
                    state.copy(
                        currentSteps = event.currentStepCount
                    )
                }
            }

            is StepsEvent.SetGoal -> {
                _stepsState.update { state ->
                    state.copy(
                        goal = event.goal
                    )
                }
            }

            is StepsEvent.SetCaloriesBurnedInState -> {
                _stepsState.update { state ->
                    state.copy(
                        caloriesBurned = event.calories
                    )
                }
            }

            is StepsEvent.SetAverageStepsInState -> {
                _stepsState.update { state ->
                    state.copy(
                        averageSteps = event.avgSteps
                    )
                }
            }

            is StepsEvent.SetDataList -> {
                _stepsState.update { state ->
                    state.copy(
                        stepsData = event.listData
                    )
                }
            }
        }
    }

    private fun keepStateUpToDate() {
        viewModelScope.launch(context = Dispatchers.IO) {
            goalRepository.readGoalValue().collect { value ->
                onEvent(event = StepsEvent.SetGoal(goal = value))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            stepsDao.getEverything().collect { stepsDataList ->
                onEvent(StepsEvent.SetDataList(listData = stepsDataList))
                for (stepsData in stepsDataList) {
                    if (stepsData.id == _date.value) {
                        onEvent(event = StepsEvent.SetCurrentStepCountInState(currentStepCount = stepsData.stepCount))
                        onEvent(event = StepsEvent.SetCaloriesBurnedInState(calories = stepsData.stepCount.calculateCalorieValue()))
                    }
                }
            }
        }
        viewModelScope.launch(context = Dispatchers.IO) {
            stepsDao.getAverageStepValue().collect {
                onEvent(event = StepsEvent.SetAverageStepsInState(it))
            }
        }
    }

    fun alterGoal(goal: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            goalRepository.saveGoalValue(goalValue = goal)
        }
    }

    private fun keepSensorStatusUpToDate() {
        viewModelScope.launch(context = Dispatchers.IO) {
            statusRepository.readListeningState().collect { status ->
                _isTheSensorActive.value = status
            }
        }
    }
}