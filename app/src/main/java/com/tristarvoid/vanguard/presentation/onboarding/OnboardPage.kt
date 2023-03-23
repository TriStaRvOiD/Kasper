package com.tristarvoid.vanguard.presentation.onboarding

import androidx.annotation.DrawableRes
import com.tristarvoid.vanguard.R

sealed class OnboardPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnboardPage(
        image = R.drawable.exercise,
        title = "Meeting",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object Second : OnboardPage(
        image = R.drawable.exercise,
        title = "Coordination",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object Third : OnboardPage(
        image = R.drawable.exercise,
        title = "Dialogue",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}