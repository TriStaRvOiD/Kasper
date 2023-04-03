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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.domain.use_cases.HolderViewModel
import com.tristarvoid.vanguard.util.Header
import com.tristarvoid.vanguard.util.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    navControl: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    holderViewModel: HolderViewModel,
    items: List<MenuItem>,
) {
    val index = remember {
        holderViewModel.concernedItem
    }
    val scrollState = rememberScrollState()
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .verticalScroll(state = scrollState, enabled = true)
            .fillMaxHeight()
    ) {
        Header(
            modifier = Modifier.fillMaxWidth().padding(64.dp),
            text = stringResource(id = R.string.app_name)
        )
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
                    val destination = item.id //Identifies the screen to which we navigate to.
                    val current = navControl.currentDestination?.route  //The current screen the user is at.
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
