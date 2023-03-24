/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.util.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Vanguard", fontSize = 30.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    navControl: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navViewModel: NavViewModel,
    items: List<MenuItem>,
) {
    val index = remember {
        navViewModel.concernedItem
    }
    val scrollState = rememberScrollState()
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .verticalScroll(state = scrollState, enabled = true)
            .fillMaxHeight()
    ) {
        DrawerHeader()
        Divider(
            color = Color.DarkGray
        )
        items.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(21.dp)
                    )
                },
                label = {
                    Text(item.title)
                },
                selected = item == items[index.value],
                onClick = {
                    val destination = item.title + "_screen"
                    val current = navControl.currentDestination?.route
                    if (current != destination) {
                        if (current != ScreenConfiguration.HomeScreen.route)
                            navControl.popBackStack()
                        if (destination != ScreenConfiguration.HomeScreen.route) {
                            navControl.navigate(route = destination)
                        }
                    }
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(4.dp)
            )
            if (item == items[0] || item == items[5])
                Divider(
                    color = Color.DarkGray
                )
        }
    }
}
