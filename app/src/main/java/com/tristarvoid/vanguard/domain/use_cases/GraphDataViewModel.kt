/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.domain.use_cases

import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.util.*

class GraphDataViewModel : ViewModel() {

    private val min = 0f
    private val max = 16f
    private val rand = Random()
    private val fl = rand.nextFloat() * (max - min) + min

    private fun getRandomEntries() = List(4) {
        FloatEntry(
            it.toFloat(),
            fl
        )
    }

    val chartEntryModelProducer = ChartEntryModelProducer(getRandomEntries())
}