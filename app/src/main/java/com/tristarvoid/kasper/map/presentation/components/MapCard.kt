/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.map.presentation.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.logo.generated.LogoSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import com.tristarvoid.kasper.BuildConfig
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.core.presentation.components.CustomCard
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import com.tristarvoid.kasper.location.services.LocationService
import com.tristarvoid.kasper.core.presentation.utils.Region
import com.tristarvoid.kasper.core.presentation.utils.isDark

@OptIn(MapboxExperimental::class)
@Composable
fun MapCard(
    modifier: Modifier,
    isActive: Boolean,
    isLocationPermissionGranted: Boolean,
    region: Region?,
    context: Context = LocalContext.current,
    handleLocationPermission: () -> Unit
) {
    if (isLocationPermissionGranted) {
        var fetchedLocation by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(region) {
            if (region != null) {
                fetchedLocation = true
            }
        }
        val locationServiceIntent = Intent(context, LocationService::class.java)
        MapboxOptions.accessToken = BuildConfig.mapbox_public_token
        if (!isActive)
            CustomCard(modifier = modifier, onClick = {
                locationServiceIntent.apply {
                    action = LocationService.ACTION_LOCATION_TRACKING_ENABLE
                    context.startService(this)
                }
            }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "disabled.",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = JosefinSans
                    )
                    Text(
                        text = "tap to enable.",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = JosefinSans
                    )
                }
            }
        else if (fetchedLocation) {
            if (MaterialTheme.colorScheme.isDark())
                CustomCard(modifier = modifier, elevation = 10) {
                    MapboxMap(
                        mapViewportState = MapViewportState().apply {
                            setCameraOptions {
                                zoom(17.0)
                                center(Point.fromLngLat(region!!.longitude, region.latitude))
//                                pitch(0.0)
//                                bearing(0.0)
                            }
                        },
                        mapInitOptionsFactory = { context ->
                            MapInitOptions(
                                context = context,
                                styleUri = Style.DARK,
                            )
                        },
                        compassSettings = CompassSettings { enabled = false },
                        attributionSettings = AttributionSettings { enabled = false },
                        logoSettings = LogoSettings { enabled = false },
                        scaleBarSettings = ScaleBarSettings { enabled = false },
                        onMapLongClickListener = {
                            locationServiceIntent.apply {
                                action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
                                context.startService(this)
                            }
                            true
                        }
                    ) {
                        AddPointer(
                            point = Point.fromLngLat(region!!.longitude, region.latitude),
                            context = context
                        )
                    }
                }
            else
                CustomCard(modifier = modifier, elevation = 10) {
                    MapboxMap(
                        mapViewportState = MapViewportState().apply {
                            setCameraOptions {
                                zoom(17.0)
                                center(Point.fromLngLat(region!!.longitude, region.latitude))
                                pitch(0.0)
                                bearing(0.0)
                            }
                        },
                        mapInitOptionsFactory = { context ->
                            MapInitOptions(
                                context = context,
                                styleUri = Style.LIGHT,
                            )
                        },
                        attributionSettings = AttributionSettings { enabled = false },
                        logoSettings = LogoSettings { enabled = false },
                        scaleBarSettings = ScaleBarSettings { enabled = false },
                        onMapLongClickListener = {
                            locationServiceIntent.apply {
                                action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
                                context.startService(this)
                            }
                            true
                        }
                    ) {
                        AddPointer(
                            point = Point.fromLngLat(region!!.longitude, region.latitude),
                            context = context
                        )
                    }
                }
        } else
            CustomCard(modifier = modifier, onClick = {
                locationServiceIntent.apply {
                    action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
                    context.startService(this)
                }
            }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "loading . . .",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = JosefinSans
                    )
                }
            }
    } else
        CustomCard(modifier = modifier, onClick = handleLocationPermission) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "permission is not granted.",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = JosefinSans
                )
                Text(
                    text = "tap to grant.",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = JosefinSans
                )
            }
        }
}

@OptIn(MapboxExperimental::class)
@Composable
private fun AddPointer(point: Point, context: Context) {
    val drawableRes =
        if (MaterialTheme.colorScheme.isDark()) R.drawable.location_for_dark_svgrepo_com else R.drawable.location_for_light_svgrepo_com

    val drawable = ResourcesCompat.getDrawable(
        context.resources,
        drawableRes,
        LocalContext.current.theme
    )
    val bitmap = drawable!!.toBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    PointAnnotation(
        iconImageBitmap = bitmap,
        iconSize = 1.5,
        point = point,
    )
}