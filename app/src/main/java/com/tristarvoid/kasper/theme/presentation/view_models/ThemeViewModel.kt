/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.theme.presentation.view_models

import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.theme.data.DynamicPrefDataStoreRepository
import com.tristarvoid.kasper.theme.data.ThemeModeDataStoreRepository
import com.tristarvoid.kasper.core.presentation.utils.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeModeDataStoreRepository,
    private val dynamicRepository: DynamicPrefDataStoreRepository,
    private val configuration: Configuration
) : ViewModel() {
    private val _themePref = MutableStateFlow<String?>(null)
    val themePref = _themePref.asStateFlow()

    private val _dynamicEnabled = MutableStateFlow<Boolean?>(null)
    val dynamicEnabled = _dynamicEnabled.asStateFlow()

    init {
        retrieveThemeMode()
        retrieveDynamicPref()
    }

    private fun retrieveThemeMode() {
        viewModelScope.launch(context = Dispatchers.IO) {
            themeRepository.readThemeMode().collect { mode ->
                _themePref.value = mode
            }
        }
    }

    private fun retrieveDynamicPref() {
        viewModelScope.launch(context = Dispatchers.IO) {
            dynamicRepository.readDynamicPref().collect { pref ->
                _dynamicEnabled.value = pref
            }
        }
    }

    private fun updateThemeMode(themeValue: ThemeMode) {
        viewModelScope.launch(context = Dispatchers.IO) {
            themeRepository.saveThemeMode(themeValue.toString())
        }
    }

    fun toggleThemeMode() {
        viewModelScope.launch {
            try {
                when (_themePref.value!!) {
                    ThemeMode.DARK.toString() -> {
                        updateThemeMode(ThemeMode.LIGHT)
                        dynamicRepository.saveDynamicPref(true)
                    }
                    ThemeMode.LIGHT.toString() -> {
                        updateThemeMode(ThemeMode.DARK)
                        dynamicRepository.saveDynamicPref(false)
                    }
                    ThemeMode.SYSTEM.toString() -> {
                        if (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                            updateThemeMode(ThemeMode.LIGHT)
                        else
                            updateThemeMode(ThemeMode.DARK)
                    }
                }
            } catch (e: Exception) {
                Log.d("Here", e.message.toString())
            }
        }
    }

    fun setDynamicPref(value: Boolean) {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                dynamicRepository.saveDynamicPref(value)
            } catch (e: Exception) {
                Log.d("Here", e.message.toString())
            }
        }
    }
}
