/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.core.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    modifier: Modifier = Modifier,
    parts: List<String>
) {
    var partIndex by remember { mutableIntStateOf(0) }
    var partText by remember { mutableStateOf("") }
    val textToDisplay = partText

    LaunchedEffect(key1 = parts) {
        while (partIndex < parts.size) {
            val part = parts[partIndex]

            part.forEachIndexed { charIndex, _ ->
                partText = part.substring(startIndex = 0, endIndex = charIndex + 1)
                delay(100)
            }

            if (partIndex != parts.lastIndex) {
                delay(3000)

                part.forEachIndexed { charIndex, _ ->
                    partText = part.substring(startIndex = 0, endIndex = part.length - (charIndex + 1))
                    delay(30)
                }
            }

            delay(500)

            partIndex += 1
        }
    }

    Text(
        modifier = modifier,
        text = textToDisplay,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.W400,
        fontFamily = JosefinSans
    )
}