/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.utils

//Puts a comma after every 3 digits
fun Int.formatDecimalSeparator(): String {
    return toString().reversed().chunked(3).joinToString(",").reversed()
}

//This method will change in the future.
// Here weight is 70kg, height is 6 ft
fun Int.calculateCalorieValue(): Int {
    return ((0.57 * 70) + (0.415 * 6) + (0.00063 * this)).toInt()
}

