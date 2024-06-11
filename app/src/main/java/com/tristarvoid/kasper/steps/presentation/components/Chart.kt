/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.presentation.components

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.chart.zoom.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.theme.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.theme.VicoTheme
import com.patrykandpatrick.vico.compose.theme.vicoTheme
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.chart.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.columnSeries
import com.patrykandpatrick.vico.core.scroll.Scroll
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.steps.data.DailyStepsData
import com.tristarvoid.kasper.steps.presentation.StepsState
import com.tristarvoid.kasper.core.presentation.components.rememberMarker
import com.tristarvoid.kasper.core.presentation.utils.isDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.Locale

@Composable
fun WeeklyChart(
    modifier: Modifier,
    stepsState: StepsState,
    context: Context = LocalContext.current
) {
    val dailyStepData = stepsState.dailyStepData.takeLast(7)
    var convertedList = listConversionHelper(data = dailyStepData)
    var averageValue = convertedList.map { it.totalStepCount }.average().toFloat()
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    LaunchedEffect(dailyStepData, LocalDate.now()) {
        convertedList = listConversionHelper(data = dailyStepData)
        averageValue = convertedList.map { it.totalStepCount }.average().toFloat()
        withContext(Dispatchers.Default) {
            modelProducer.tryRunTransaction {
                columnSeries {
                    repeat(3) { index ->
                        when (index) {
                            0 -> series(convertedList.map { it.indoorStepCount })
                            1 -> series(convertedList.map { it.outdoorStepCount })
                            2 -> series(convertedList.map { it.totalStepCount - (it.indoorStepCount + it.outdoorStepCount) })
                        }
                    }
                }
            }
        }
    }
    ProvideVicoTheme(
        theme = VicoTheme(
            cartesianLayerColors = if (MaterialTheme.colorScheme.isDark()) listOf(
                Color(0xffcacaca),
                Color(0xffa8a8a8),
                Color(0xff888888)
            ) else listOf(Color(0xff787878), Color(0xff5a5a5a), Color(0xff383838)),
            elevationOverlayColor = if (MaterialTheme.colorScheme.isDark()) Color(0xffffffff) else Color(
                0x00000000
            ),
            lineColor = if (MaterialTheme.colorScheme.isDark()) Color(0xff555555) else Color(
                0x47000000
            ),
            textColor = if (MaterialTheme.colorScheme.isDark()) Color(0xffffffff) else Color(
                0xde000000
            )
        )
    ) {
        CartesianChartHost(
            chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                    ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            color = color1,
                            thickness = COLUMN_THICKNESS_DP.dp,
                            shape =
                            Shapes.roundedCornerShape(
                                bottomLeftPercent = COLUMN_ROUNDNESS_PERCENT,
                                bottomRightPercent = COLUMN_ROUNDNESS_PERCENT,
                            ),
                        ),
                        rememberLineComponent(
                            color = color2,
                            thickness = COLUMN_THICKNESS_DP.dp,
                        ),
                        rememberLineComponent(
                            color = color3,
                            thickness = COLUMN_THICKNESS_DP.dp,
                            shape =
                            Shapes.roundedCornerShape(
                                topLeftPercent = COLUMN_ROUNDNESS_PERCENT,
                                topRightPercent = COLUMN_ROUNDNESS_PERCENT,
                            ),
                        ),
                    ),
                    mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
                ),
                startAxis =
                rememberStartAxis(
                    label = rememberAxisLabelComponent(
                        typeface = ResourcesCompat.getFont(context, R.font.josefinsans_regular)!!
                    ),
                    itemPlacer = startAxisItemPlacer,
                    labelRotationDegrees = AXIS_LABEL_ROTATION_DEGREES,
                ),
                bottomAxis =
                rememberBottomAxis(
                    label = rememberAxisLabelComponent(
                        typeface = ResourcesCompat.getFont(context, R.font.josefinsans_regular)!!
                    ),
                    valueFormatter = rememberWeeklyAxisValueFormatter(),
                    itemPlacer =
                    remember {
                        AxisItemPlacer.Horizontal.default(
                            spacing = 3,
                            addExtremeLabelPadding = true
                        )
                    },
                ),
                legend = rememberLegend(context),
                decorations = listOf(rememberComposeHorizontalLine(context, averageValue)),
            ),
            modelProducer = modelProducer,
            modifier = modifier,
            marker = rememberMarker(),
            scrollState = scrollState,
            horizontalLayout = HorizontalLayout.fullWidth(),
            runInitialAnimation = true,
            zoomState = rememberVicoZoomState(zoomEnabled = false),
        )
    }
}

@Composable
private fun rememberComposeHorizontalLine(
    context: Context,
    averageValue: Float
): HorizontalLine {
    val color = Color(0xFFFCD209)
    return rememberHorizontalLine(
        y = { averageValue },
        line = rememberLineComponent(color, HORIZONTAL_LINE_THICKNESS_DP.dp),
        labelComponent =
        rememberTextComponent(
            background = rememberShapeComponent(Shapes.pillShape, color),
            padding =
            dimensionsOf(
                HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP.dp,
                HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP.dp,
            ),
            margins = dimensionsOf(HORIZONTAL_LINE_LABEL_MARGIN_DP.dp),
            typeface = ResourcesCompat.getFont(context, R.font.josefinsans_regular)!!
        ),
    )
}

@Composable
private fun rememberLegend(context: Context) =
    rememberHorizontalLegend(
        items =
        chartColors.mapIndexed { index, chartColor ->
            rememberLegendItem(
                icon = rememberShapeComponent(Shapes.pillShape, chartColor),
                label =
                rememberTextComponent(
                    color = vicoTheme.textColor,
                    textSize = 12.sp,
                    typeface = ResourcesCompat.getFont(context, R.font.josefinsans_regular)!!
                ),
                labelText = when (index) {
                    0 -> "Indoor steps"
                    1 -> "Outdoor steps"
                    2 -> "Undetermined"
                    else -> ""
                },
            )
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 12.dp,
        padding = dimensionsOf(top = 8.dp),
    )

@Composable
private fun rememberWeeklyAxisValueFormatter(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    return AxisValueFormatter { x, _, _ ->
        val date = LocalDate.now().minusDays(6)
            .plusDays(
                x.toInt().toLong()
            )
        "${weekdayNames[date.plusDays(1).dayOfWeek.value]} ${date.dayOfMonth}"
    }
}

private fun listConversionHelper(data: List<DailyStepsData>): List<DailyStepsData> {
    val currentEpochDay = LocalDate.now().toEpochDay()
    val epochDaysToCheck = (currentEpochDay - 6..currentEpochDay).toList()

    val dataForCurrentWeek = mutableListOf<DailyStepsData>()
    for (epochDay in epochDaysToCheck) {
        val dailyStepsData = data.find { it.epochDay == epochDay }
        dataForCurrentWeek.add(dailyStepsData ?: DailyStepsData(epochDay, 0, 0, 0))
    }
    return dataForCurrentWeek
}

private const val COLUMN_ROUNDNESS_PERCENT: Int = 40
private const val COLUMN_THICKNESS_DP: Int = 10
private const val AXIS_LABEL_ROTATION_DEGREES = 45f

private val color1 = Color(0xFF8B008B)
private val color2 = Color(0xFF920B3E)
private val color3 = Color(0xFF009494)
private val chartColors = listOf(Color(0xFF8B008B), Color(0xFFE0115F), Color(0xFF009494))
private val startAxisItemPlacer = AxisItemPlacer.Vertical.count({ 3 })

private const val HORIZONTAL_LINE_THICKNESS_DP = 2f
private const val HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP = 8f
private const val HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP = 2f
private const val HORIZONTAL_LINE_LABEL_MARGIN_DP = 4f

private val weekdayNames = DateFormatSymbols.getInstance(Locale.ENGLISH).shortWeekdays
