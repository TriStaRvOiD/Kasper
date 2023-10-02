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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.presentation.components.CustomCard
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.utils.isDark

@Composable
fun CalorieCard(
    modifier: Modifier,
    calories: String
) {
    CustomCard(
        enableGlow = MaterialTheme.colorScheme.isDark(),
        color1 = Color(0xFFBDFF42),
        color2 = Color(0xFFF44336),
        modifier = modifier,
        onClick = {}
    ) {
        Row(modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
            Text(
                text = "Calories",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = JosefinSans
            )
            Icon(
                modifier = Modifier.size(18.dp).padding(top = 2.dp),
                painter = painterResource(id = R.drawable.fire),
                contentDescription = "Fire icon"
            )
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = "$calories kcal",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = JosefinSans
        )
    }
}