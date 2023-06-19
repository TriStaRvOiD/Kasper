/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun LottieLoader(
    jsonResource: Int,
    size: Int = 300,
    repeat: Boolean = true,
    alignment: Alignment = Alignment.Center
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(jsonResource))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )
    if (repeat)
        LottieAnimation(
            modifier = Modifier.size(size.dp),
            composition = composition,
            progress = { progress },
            alignment = alignment
        )
    else
        LottieAnimation(
            modifier = Modifier.size(size.dp),
            composition = composition,
            alignment = alignment
        )
}