/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.screens.home

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.view.CustomCard

@Composable
fun Weather(
    desc: String,
    temp: String,
    quality: String
) {
    CustomCard(
        modifier = Modifier
            .widthIn(min = 178.dp, max = 178.dp)
            .heightIn(min = 145.dp, max = 145.dp),
        function = {}
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = "today : ",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = JosefinSans
        )
        if (temp != "" && desc != "" && quality != "") {
            Text(
                modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                text = "\u2022 It's $temp\u2103 , with ${desc.lowercase()}.",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = JosefinSans
            )
            Text(
                modifier = Modifier.padding(10.dp),
                text = "\u2022 The quality of air outdoors is $quality.",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = JosefinSans
            )
        }
        else
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Loading . . .",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                fontFamily = JosefinSans
            )
    }
}