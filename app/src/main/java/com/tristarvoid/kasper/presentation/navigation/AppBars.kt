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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    navControl: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    holderViewModel: HolderViewModel,
    actionEnabled: Boolean = true
) {
    val heading by remember {
        holderViewModel.mainHeading
    }
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                modifier = Modifier,
                text = heading,
                fontFamily = JosefinSans
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.menu),
                    modifier = Modifier
                        .size(30.dp),
                    contentDescription = "Toggle drawer"
                )
            }
        },
        actions = {
            if (actionEnabled)
                IconButton(
                    onClick = {
                        navControl.navigate(ScreenConfiguration.CalendarView.route)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.calendar),
                        modifier = Modifier
                            .size(25.dp),
                        contentDescription = "Open calendar"
                    )
                }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FragmentAppBar(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
    singular: Boolean = true
) {
    val heading by remember {
        holderViewModel.fragHeading
    }
    MediumTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                modifier = Modifier,
                text = heading,
                fontFamily = JosefinSans
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navControl.popBackStack()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (singular) R.drawable.close else R.drawable.back),
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = if (singular) "Close" else "Go back"
                )
            }
        }
    )
}
