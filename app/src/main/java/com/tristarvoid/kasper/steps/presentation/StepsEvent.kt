/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.presentation

import com.tristarvoid.kasper.steps.data.DailyStepsData
import com.tristarvoid.kasper.steps.data.StepsData

sealed interface StepsEvent {
    data class SetCurrentStepCountInState(val currentStepCount: Int): StepsEvent
    data class SetAverageStepsInState(val avgSteps: Int): StepsEvent
    data class SetGoal(val goal: Int): StepsEvent
    data class SetCaloriesBurnedInState(val calories: Int): StepsEvent
    data class SetDataList(val listData: List<StepsData>): StepsEvent
    data class setDailyStepDataList(val listData: List<DailyStepsData>): StepsEvent
}