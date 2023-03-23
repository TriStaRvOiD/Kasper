package com.tristarvoid.vanguard.presentation.resources.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tristarvoid.vanguard.R

@Composable
fun Nutrition(
    padding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieLoader(R.raw.food)
//        Text(
//            modifier = Modifier.padding(top = padding.calculateTopPadding()),
//            text = "Nutrition",
//            textAlign = TextAlign.Center
//        )
    }
}