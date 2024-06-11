/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.weather.data.retrofit

import com.tristarvoid.kasper.weather.data.retrofit.extra.Clouds
import com.tristarvoid.kasper.weather.data.retrofit.extra.Coord
import com.tristarvoid.kasper.weather.data.retrofit.extra.Main
import com.tristarvoid.kasper.weather.data.retrofit.extra.Rain
import com.tristarvoid.kasper.weather.data.retrofit.extra.Sys
import com.tristarvoid.kasper.weather.data.retrofit.extra.WeatherX
import com.tristarvoid.kasper.weather.data.retrofit.extra.Wind

data class Weather(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: Wind
)