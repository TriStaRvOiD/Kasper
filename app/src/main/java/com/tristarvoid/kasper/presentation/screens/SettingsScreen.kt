/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.presentation.navigation.MainAppBar
import com.tristarvoid.kasper.view.Header
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Scaffold(
        topBar = {
            MainAppBar(navControl, drawerState, scope, holderViewModel, false)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        )
        {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(5.dp),
                text = "Settings"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider()
        }
    }
}