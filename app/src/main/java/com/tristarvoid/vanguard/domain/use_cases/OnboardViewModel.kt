package com.tristarvoid.vanguard.domain.use_cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.vanguard.data.repo.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardViewModel(
    private val repository: DataStoreRepository
    ) : ViewModel() {

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOnBoardingState(completed = completed)
        }
    }
}