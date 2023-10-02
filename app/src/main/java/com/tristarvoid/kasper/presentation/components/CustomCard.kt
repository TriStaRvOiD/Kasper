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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    enableGlow: Boolean = false,
    color1: Color = Color(0xFFff51eb),
    color2: Color = Color(0xFFede342),
    loopAnimate: Boolean = false,
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val firstColor = remember { color1 }
    val secondColor = remember { color2 }

    val startColor by animateColorAsState(
        targetValue = firstColor,
        animationSpec = tween(500), label = ""
    )

    val endColor by animateColorAsState(
        targetValue = secondColor,
        animationSpec = tween(500), label = ""
    )

    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .shadow(
                elevation = 40.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                ambientColor = if (enableGlow) startColor else Color.Transparent,
                spotColor = if (enableGlow) endColor else Color.Transparent,
            )
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                brush = Brush.linearGradient(
                    colors = if (enableGlow) listOf(
                        startColor,
                        endColor
                    ) else listOf(MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.onBackground),
                    Offset(0.0f, 0.0f), Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(8.dp),
            )
    ) {
        content()
    }
}