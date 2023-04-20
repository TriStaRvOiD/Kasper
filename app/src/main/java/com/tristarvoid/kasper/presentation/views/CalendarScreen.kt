/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.presentation.navigation.FragmentAppBar
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    navControl: NavHostController,
    holderViewModel: HolderViewModel
) {
    BackHandler(enabled = true) {
        navControl.popBackStack()
    }
    val currentMonth = remember {
        YearMonth.now()
    }
    val startMonth = remember {
        currentMonth.minusMonths(0)
    } // Adjust as needed
    val endMonth = remember {
        currentMonth.plusMonths(100)
    } // Adjust as needed
    val firstDayOfWeek = remember {
        firstDayOfWeekFromLocale()
    }
    val daysOfWeek = daysOfWeek()// Available from the library
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            FragmentAppBar(
                navControl = navControl,
                holderViewModel = holderViewModel
            )
        }
    ) { values ->
        Box(
            modifier = Modifier
                .padding(top = values.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            HorizontalCalendar(
                state = state,
                dayContent = { Day(it) },
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title as month header
                }
            )
        }
    }
}

@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}