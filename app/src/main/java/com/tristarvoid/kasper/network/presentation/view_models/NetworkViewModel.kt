/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.network.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.network.data.ConnectivityObserver
import com.tristarvoid.kasper.network.data.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val networkStatus = networkConnectivityObserver.observe()

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()

    init {
        viewModelScope.launch {
            networkStatus.collect { status ->
                _isNetworkAvailable.value = status == ConnectivityObserver.Status.Available
            }
        }
    }
}