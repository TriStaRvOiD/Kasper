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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.data.retrofit.quote.QuoteApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteApi: QuoteApi
) : ViewModel() {

    private val _quote = MutableStateFlow("")
    val quote = _quote.asStateFlow()

    fun getTheQuote() {
        viewModelScope.launch {
            try {
                val response = quoteApi.getQuote()
                if (response.isSuccessful && response.body() != null) {
                    _quote.value = response.body()!![0].content
                }
            }
            catch (e: IOException) {
                Log.d("Quote", "Internet")
                return@launch
            }
            catch (e: HttpException) {
                Log.d("Quote", "Internet")
                return@launch
            }
        }
    }
}