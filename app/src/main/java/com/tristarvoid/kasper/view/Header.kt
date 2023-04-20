/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans

@Composable
fun Header(
    modifier: Modifier,
    alignment: Alignment = Alignment.Center,
    text: String,
    fontSize: Int = 30
) {
    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontFamily = JosefinSans
        )
    }
}