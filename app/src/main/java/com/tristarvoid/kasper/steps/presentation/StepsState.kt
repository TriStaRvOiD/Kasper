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

data class StepsState(
    val stepsData: List<StepsData> = emptyList(),
    val dailyStepData: List<DailyStepsData> = emptyList(),
    val currentSteps: Int = 0,
    val averageSteps: Int = 0,
    val goal: Int = 0,
    val caloriesBurned: Int = 0
)
