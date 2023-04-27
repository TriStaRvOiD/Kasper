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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import com.tristarvoid.kasper.data.steps.StepsData
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.presentation.navigation.FragmentAppBar
import kotlinx.coroutines.flow.filter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
    stepsViewModel: StepsViewModel = hiltViewModel()
) {
    BackHandler(enabled = true) {
        navControl.popBackStack()
    }
    val stepsState by stepsViewModel.state.collectAsState()
    val dataList = stepsState.stepsData
    val currentDate = remember {
        LocalDate.now()
    }
    val startDate = remember {
        if (dataList.isNotEmpty())
            LocalDate.ofEpochDay(dataList[0].id)
        else
            currentDate
    }
    val endDate = remember {
        currentDate.plusDays(500)
    }
    val selection = remember {
        mutableStateOf(currentDate)
    }
    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
    holderViewModel.fragHeading.value = getWeekPageTitle(visibleWeek)
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
        Column(
            modifier = Modifier
                .padding(top = values.calculateTopPadding())
                .fillMaxSize()
        ) {
            WeekCalendar(
                state = state,
                dayContent = { day ->
                    Day(day.date, isSelected = selection.value == day.date) { clicked ->
                        if (selection.value != clicked) {
                            selection.value = clicked
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            DayContent(epochDay = selection.value.toEpochDay(), dataList)
        }
    }
}

@Composable
fun DayContent(
    epochDay: Long,
    dataList: List<StepsData>
) {
    for (i in dataList) {
        if (i.id == epochDay) {
            Text(text = "Step count = ${i.currentSteps}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Goal = ${i.goal}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Steps Remaining = ${
                    if (i.goal > i.currentSteps)
                        i.goal - i.currentSteps
                    else
                        0
                }"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Calories burned = ${i.calories}")
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.displayText(),
                fontSize = 12.sp
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 14.sp,
                color = if (isSelected && isSystemInDarkTheme()) Color.Yellow else if (isSelected && !isSystemInDarkTheme()) Color.Cyan else LocalContentColor.current,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(if (isSystemInDarkTheme()) Color.Yellow else Color.Cyan)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }

        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }

        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}
