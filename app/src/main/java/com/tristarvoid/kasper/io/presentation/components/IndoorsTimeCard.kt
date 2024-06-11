/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.io.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tristarvoid.kasper.core.presentation.components.CustomCard
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import com.tristarvoid.kasper.core.presentation.utils.isDark
import java.time.Duration

@Composable
fun IndoorsTimeCard(modifier: Modifier, duration: Duration) {
    val hoursState = remember { mutableLongStateOf(0L) }
    val minutesState = remember { mutableLongStateOf(0L) }
    val secondsState = remember { mutableLongStateOf(0L) }

    LaunchedEffect(duration) {
        hoursState.longValue = duration.toHours()
        minutesState.longValue = duration.minusHours(hoursState.longValue).toMinutes()
        secondsState.longValue =
            duration.minusHours(hoursState.longValue).minusMinutes(minutesState.longValue).seconds
    }

    CustomCard(
        enableGlow = MaterialTheme.colorScheme.isDark(),
//        color1 = Color(0xFFE0115F),
//        color2 = Color(0xFF009494),
        color1 = Color(0xFFE0115F),
        color2 = Color(0xFFFCD209),
        elevation = 10,
        modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Indoor time : ",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = JosefinSans
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp, end = 10.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hoursState.longValue > 0) {
                    Text(
                        text = "%2d hr ".format(hoursState.longValue),
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = JosefinSans
                    )
                }
                if (minutesState.longValue > 0 || hoursState.longValue > 0) {
                    Text(
                        text = "%2d min ".format(minutesState.longValue),
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = JosefinSans
                    )
                }
                Text(
                    text = "%2d sec".format(secondsState.longValue),
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = JosefinSans
                )
            }
        }
    }
}