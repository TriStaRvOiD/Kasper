package com.tristarvoid.vanguard.domain.use_cases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.vanguard.data.repo.DataStoreRepository
import com.tristarvoid.vanguard.presentation.navigation.ScreenConfiguration
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> =
        mutableStateOf("")
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = ScreenConfiguration.HomeScreen.route
                } else {
                    _startDestination.value = ScreenConfiguration.WelcomeScreen.route
                }
            }
            _isLoading.value = false
        }
    }
}