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
import com.tristarvoid.kasper.data.datastore.ThemeDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeDataStoreRepository
) : ViewModel() {
    var dynamicEnabled = MutableStateFlow(false)
    var isDarkTheme = MutableStateFlow<Boolean?>(null)

    init {
        retrieveThemeValue()
    }

    private fun retrieveThemeValue() {
        viewModelScope.launch(context = Dispatchers.IO) {
            themeRepository.readThemeValue().collect { value ->
                if (value == "light")
                    isDarkTheme.value = false
                else if (value == "dark")
                    isDarkTheme.value = true
            }
        }
    }

    fun writeThemeValue(themeValue: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            themeRepository.saveThemeValue(themeValue)
        }
    }
}
