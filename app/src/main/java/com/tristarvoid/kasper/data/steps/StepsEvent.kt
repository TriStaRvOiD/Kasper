/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.data.steps

sealed interface StepsEvent {
    object SaveEntry: StepsEvent
    data class SetCurrentSteps(val steps: Int): StepsEvent
    data class SetGoal(val goal: Int): StepsEvent
    data class SetCalories(val calories: Int): StepsEvent
    data class SetAvgSteps(val avgSteps: Int): StepsEvent
    data class SetDataList(val listData: List<StepsData>): StepsEvent
    data class SetRemainingSteps(val remainingSteps: Int): StepsEvent
}