/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.core.presentation.utils.calculateCalorieValue
import com.tristarvoid.kasper.steps.data.GoalDataStoreRepository
import com.tristarvoid.kasper.steps.data.StepServiceStatusDataStoreRepository
import com.tristarvoid.kasper.io.data.IODao
import com.tristarvoid.kasper.steps.data.DailyStepsDao
import com.tristarvoid.kasper.steps.data.DailyStepsData
import com.tristarvoid.kasper.steps.data.StepsDao
import com.tristarvoid.kasper.steps.presentation.StepsEvent
import com.tristarvoid.kasper.steps.presentation.StepsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val stepsDao: StepsDao,
    private val dailyStepsDao: DailyStepsDao,
    private val ioDao: IODao,
    private val statusRepository: StepServiceStatusDataStoreRepository,
    private val goalRepository: GoalDataStoreRepository
) : ViewModel() {

    private val _stepsState = MutableStateFlow(value = StepsState())
    val stepsState = _stepsState.asStateFlow()

    private val _isStepServiceActive = MutableStateFlow(value = false)
    val isStepServiceActive = _isStepServiceActive.asStateFlow()

    init {
        keepServiceStatusUpToDate()
        populateDailyStepData()
        keepStateUpToDate()
    }

    private fun populateDailyStepData() {
        viewModelScope.launch(context = Dispatchers.IO) {
            stepsDao.getAllData().collect { stepsDataList ->
                onEvent(StepsEvent.SetDataList(listData = stepsDataList))

                val epochDays = stepsDataList.map { it.epochDay }.distinct()

                epochDays.map { epochDay ->

                    val concernedStepsDataList = stepsDataList.filter { it.epochDay == epochDay }
                    val totalStepCount = concernedStepsDataList.size

                    var indoorStepCount = 0
                    var outdoorStepCount = 0

                    val concernedIOList = ioDao.getIOList(epochDay).take(1).single()

                    concernedStepsDataList.forEach { stepsData ->

                        val userIOState = concernedIOList
                            .asSequence()
                            .filter { it.dayNanos <= stepsData.stepNanos }
                            .lastOrNull()

                        if (userIOState != null) {
                            if (userIOState.isIndoors == true) {
                                indoorStepCount++
                            } else if (userIOState.isIndoors == false) {
                                outdoorStepCount++
                            }
                        }
                    }
                    dailyStepsDao.upsertEntry(
                        DailyStepsData(
                            epochDay = epochDay,
                            totalStepCount = totalStepCount,
                            indoorStepCount = indoorStepCount,
                            outdoorStepCount = outdoorStepCount
                        )
                    )
                }
            }
        }
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

            is StepsEvent.setDailyStepDataList -> {
                _stepsState.update { state ->
                    state.copy(
                        dailyStepData = event.listData
                    )
                }
            }
        }
    }

    private fun keepStateUpToDate() {
        updateCurrentStepCount()
        updateAverageSteps()
        updateGoal()
        updateDailyStepData()
    }

    private fun updateCurrentStepCount() {
        viewModelScope.launch(context = Dispatchers.IO) {
            stepsDao.getStepCountForDay(LocalDate.now().toEpochDay()).collect {
                onEvent(StepsEvent.SetCurrentStepCountInState(currentStepCount = it))
                onEvent(StepsEvent.SetCaloriesBurnedInState(it.calculateCalorieValue()))
            }
        }
    }

    private fun updateAverageSteps() {
        viewModelScope.launch(context = Dispatchers.IO) {
            stepsDao.getAverageStepCount().collect {
                onEvent(event = StepsEvent.SetAverageStepsInState(it))
            }
        }
    }

    private fun updateGoal() {
        viewModelScope.launch(context = Dispatchers.IO) {
            goalRepository.readGoalValue().collect { value ->
                onEvent(event = StepsEvent.SetGoal(goal = value))
            }
        }
    }

    private fun updateDailyStepData() {
        viewModelScope.launch(context = Dispatchers.IO) {
            dailyStepsDao.getAllEntries().collect {
                onEvent(StepsEvent.setDailyStepDataList(it))
            }
        }
    }

    fun alterGoal(goal: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            goalRepository.saveGoalValue(goalValue = goal)
        }
    }

    private fun keepServiceStatusUpToDate() {
        viewModelScope.launch(context = Dispatchers.IO) {
            statusRepository.readListeningState().collect { status ->
                _isStepServiceActive.value = status
            }
        }
    }
}