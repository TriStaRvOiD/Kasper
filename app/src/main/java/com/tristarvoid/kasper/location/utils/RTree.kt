/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.location.utils

class RTree {
    private val polygons: MutableList<Polygon> = mutableListOf()

    fun addPolygon(polygon: Polygon) {
        polygons.add(polygon)
    }

    fun search(x: Double, y: Double): Boolean {
        for (polygon in polygons) {
            if (pointInPolygon(x, y, polygon)) {
                return true
            }
        }
        return false
    }

    private fun pointInPolygon(x: Double, y: Double, polygon: Polygon): Boolean {
        val n = polygon.points.size
        var inside = false

        var p1x = polygon.points[0].latitude
        var p1y = polygon.points[0].longitude
        for (i in 0 until n + 1) {
            val p2x = polygon.points[i % n].latitude
            val p2y = polygon.points[i % n].longitude
            if (y > p1y.coerceAtMost(p2y)) {
                if (y <= p1y.coerceAtLeast(p2y)) {
                    if (x <= p1x.coerceAtLeast(p2x)) {
                        if (p1y != p2y) {
                            val xinters = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x
                            if (p1x == p2x || x <= xinters) {
                                inside = !inside
                            }
                        }
                    }
                }
            }
            p1x = p2x
            p1y = p2y
        }
        return inside
    }
}
