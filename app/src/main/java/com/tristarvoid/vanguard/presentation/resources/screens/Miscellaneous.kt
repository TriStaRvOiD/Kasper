package com.tristarvoid.vanguard.presentation.resources.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.*
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.tristarvoid.vanguard.R

@Composable
fun Workouts(
    padding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            text = "Workouts",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Decisions(
    padding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieLoader(R.raw.decision)
//        Text(
//            modifier = Modifier.padding(top = padding.calculateTopPadding()),
//            text = "Decisions",
//            textAlign = TextAlign.Center
//        )
    }
}

@Composable
fun Reminders(
    padding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            text = "Reminders",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Water(
    padding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieLoader(R.raw.wateranimation)
//        Text(
//            modifier = Modifier.padding(top = padding.calculateTopPadding()),
//            text = "Water",
//            textAlign = TextAlign.Center
//        )
    }
}

@Composable
fun Privacy(
    padding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            text = "Privacy",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun About(
    padding: PaddingValues
) {
    LibrariesContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding.calculateTopPadding()),
        showAuthor = true,
        showVersion = true,
        showLicenseBadges = false
    )
}

@Composable
fun LottieLoader(
    jsonResource: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(jsonResource))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

