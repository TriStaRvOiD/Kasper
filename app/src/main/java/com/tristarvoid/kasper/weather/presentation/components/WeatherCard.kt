/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.weather.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tristarvoid.kasper.network.presentation.view_models.NetworkViewModel
import com.tristarvoid.kasper.weather.presentation.view_models.WeatherViewModel
import com.tristarvoid.kasper.core.presentation.components.CustomCard
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import com.tristarvoid.kasper.core.presentation.utils.isDark
import kotlinx.coroutines.delay

@Composable
fun Weather(
    modifier: Modifier,
) {
    val networkViewModel: NetworkViewModel = hiltViewModel()
    val isNetworkAvailable by networkViewModel.isNetworkAvailable.collectAsStateWithLifecycle()
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    var fetchedData by remember {
        mutableStateOf(false)
    }
    val temp by remember {
        weatherViewModel.temp
    }
    val desc by remember {
        weatherViewModel.desc
    }
    val quality by remember {
        weatherViewModel.quality
    }
    var weatherFetchStatusText by remember {
        mutableStateOf("Loading . . .")
    }
    LaunchedEffect(isNetworkAvailable, temp, desc, quality) {
        if (!isNetworkAvailable) {
            delay(3000)
            weatherFetchStatusText = "Check your network."
        } else if (temp != "" && desc != "" && quality != "") {
            weatherFetchStatusText = "Loading . . ."
            fetchedData = true
        } else {
            delay(10000)
            weatherFetchStatusText = "Something went wrong.\nPlease check your network."
        }
    }
    CustomCard(
        modifier = modifier,
        enableGlow = fetchedData && MaterialTheme.colorScheme.isDark(),
        color1 = Color(0xFFE0115F),
        color2 = Color(0xFF009494),
//        color1 = Color(0xFF8B008B),
//        color2 = Color(0xFFFCD209),
        elevation = 10
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = "Weather : ",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = JosefinSans
        )
        if (fetchedData) {
            Text(
                modifier = Modifier.padding(start = 10.dp, top = 14.dp),
                text = "\u2022 It's $temp\u2103 , with ${desc.lowercase()}.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = JosefinSans
            )
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = "\u2022 Air quality outdoors is $quality.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = JosefinSans
            )
        } else
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = weatherFetchStatusText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontFamily = JosefinSans
            )
    }
}