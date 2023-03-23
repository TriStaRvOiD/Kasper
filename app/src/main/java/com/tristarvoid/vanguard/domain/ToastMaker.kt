package com.tristarvoid.vanguard.domain

import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class ToastMaker @Inject constructor(
    private val toast: Toast
): ViewModel()
{
    fun displayToast(
        message: String,
        length: Int
    ){
        toast.setText(message)
        toast.duration = length
        toast.show()
    }
}