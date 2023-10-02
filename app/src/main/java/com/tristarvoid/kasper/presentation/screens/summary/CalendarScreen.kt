/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.screens.summary

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import com.tristarvoid.kasper.data.steps.StepsState
import com.tristarvoid.kasper.domain.StepsViewModel
import com.tristarvoid.kasper.presentation.components.CustomAppBar
import com.tristarvoid.kasper.utils.calculateCalorieValue
import com.tristarvoid.kasper.utils.displayText
import kotlinx.coroutines.flow.filter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
fun Calendar(
    title: String,
    stepsViewModel: StepsViewModel = hiltViewModel()
) {
    val stepsState by stepsViewModel.stepsState.collectAsState()
    val dataList = stepsState.stepsData
    val currentDate = remember {
        LocalDate.now()
    }
    val dataListIsEmpty by remember {
        mutableStateOf(dataList.isEmpty())
    }
    val startDate = remember {
        if (!dataListIsEmpty)
            LocalDate.ofEpochDay(dataList[0].id)
        else
            currentDate
    }
    Log.d("Start Date", startDate.toString())
    val endDate = remember {
        startDate.plusDays(dataList.size.toLong() + 10)
    }
    val selection = remember {
        mutableStateOf(value = currentDate)
    }
    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state = state)
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomAppBar(title = getWeekPageTitle(week = visibleWeek), actions = {})
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
            DayContent(epochDay = selection.value.toEpochDay(), stepsState)
        }
    }
}

@Composable
fun DayContent(
    epochDay: Long,
    stepsState: StepsState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in stepsState.stepsData) {
            if (i.id == epochDay) {
                Text(text = "Step count = ${i.stepCount}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Goal = ${stepsState.goal}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Steps Remaining = ${
                        if (stepsState.goal > i.stepCount)
                            stepsState.goal - i.stepCount
                        else
                            0
                    }"
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Calories burned = ${stepsState.currentSteps.calculateCalorieValue()}")
                Spacer(modifier = Modifier.height(10.dp))
            }
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
            verticalArrangement = Arrangement.spacedBy(space = 6.dp),
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
    var visibleWeek by remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek = state.firstVisibleWeek }
    }
    return visibleWeek
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
