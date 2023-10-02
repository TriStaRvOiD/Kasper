/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.navigation

sealed class ScreenConfiguration(val route: String) {

    data object HomeScreen : ScreenConfiguration(route = "home_screen")
    data object SummaryScreen : ScreenConfiguration(route = "summary_screen")
    data object MoreScreen : ScreenConfiguration(route = "more_screen")

    data object SettingsScreen : ScreenConfiguration(route = "settings_screen")
    data object AboutScreen : ScreenConfiguration(route = "about_screen")
    data object LicensesScreen : ScreenConfiguration(route = "licenses_screen")
}
