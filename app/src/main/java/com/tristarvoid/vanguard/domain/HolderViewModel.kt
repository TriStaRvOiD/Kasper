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
import java.util.*

class HolderViewModel : ViewModel()
{
    var dynamicEnabled = mutableStateOf(false)
    var mainHeading = mutableStateOf("")
    var fragHeading = mutableStateOf("")
    var concernedItem = mutableStateOf(0)
    var apisCalled = mutableStateOf(false)

    private val calendar = mutableStateOf(Calendar.getInstance())
    var timeOfDay = mutableStateOf(calendar.value.get(Calendar.HOUR_OF_DAY))
    fun updateTime() {
        calendar.value = Calendar.getInstance()
        timeOfDay.value = calendar.value.get(Calendar.HOUR_OF_DAY)
    }
}
