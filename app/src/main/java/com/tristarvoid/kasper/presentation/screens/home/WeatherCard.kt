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

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.data.connection.ConnectivityObserver
import com.tristarvoid.kasper.domain.NetworkViewModel
import com.tristarvoid.kasper.domain.WeatherViewModel
import com.tristarvoid.kasper.presentation.components.CustomCard
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.utils.isDark
import kotlinx.coroutines.delay

@Composable
fun Weather(
    modifier: Modifier,
    localContext: Context = LocalContext.current
) {
    val networkViewModel: NetworkViewModel = hiltViewModel()
    val networkStatus by networkViewModel.networkStatus.collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    )
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
    var loadingText by remember {
        mutableStateOf(localContext.getString(R.string.loading))
    }
    LaunchedEffect(key1 = networkStatus) {
        if (networkStatus == ConnectivityObserver.Status.Unavailable) {
            delay(1000)
            loadingText = localContext.getString(R.string.connection_unavailable)
        }
        else {
            weatherViewModel.getWeather(lat = 19.0760, long = 72.8777)
            weatherViewModel.getQuality(lat = 19.0760, long = 72.8777)
        }
    }
    LaunchedEffect(key1 = temp) {
        if (temp != "" && desc != "" && quality != "")
            fetchedData = true
    }
    CustomCard(
        enableGlow = fetchedData && MaterialTheme.colorScheme.isDark(),
        color1 = Color(0xFF2196F3),
        color2 = Color(0xFF673AB7),
        modifier = modifier,
        onClick = {}
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp),
            text = "Weather : ",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = JosefinSans
        )
        if (temp != "" && desc != "" && quality != "") {
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
                text = loadingText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontFamily = JosefinSans
            )
    }
}