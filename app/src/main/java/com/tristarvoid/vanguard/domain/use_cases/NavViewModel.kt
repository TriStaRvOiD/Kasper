package com.tristarvoid.vanguard.domain.use_cases

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NavViewModel : ViewModel()
{
    var heading = mutableStateOf("")
    var concernedItem = mutableStateOf(0)
}