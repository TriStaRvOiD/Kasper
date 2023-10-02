/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.domain.NavigationViewModel
import com.tristarvoid.kasper.domain.ThemeViewModel
import com.tristarvoid.kasper.presentation.navigation.Navigation
import com.tristarvoid.kasper.presentation.navigation.ScreenConfiguration
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.presentation.ui.theme.KasperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {
            val themeViewModel = viewModel<ThemeViewModel>()
            val dynamicEnabled by themeViewModel.dynamicEnabled.collectAsStateWithLifecycle()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()
            KasperTheme(
                darkTheme = isDarkTheme,
                dynamicColor = dynamicEnabled
            ) {
                val navController = rememberNavController()
                val navViewModel: NavigationViewModel = viewModel()
                val bottomNavigationItems = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        route = ScreenConfiguration.HomeScreen.route,
                        contentDescription = "Go to home screen",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Summary",
                        route = ScreenConfiguration.SummaryScreen.route,
                        contentDescription = "Go to summary screen",
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.calendar_filled),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.calendar_outlined)
                    ),
                    BottomNavigationItem(
                        title = "More",
                        route = ScreenConfiguration.MoreScreen.route,
                        contentDescription = "Go to more screen",
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.more),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.more)
                    )
                )

                val selectedItemIndex by navViewModel.selectedItemIndex.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.background
                            ) {
                                bottomNavigationItems.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            if (selectedItemIndex != index) {
                                                if (navController.currentBackStack.value.size > 2) {
                                                    navController.popBackStack()
                                                }
                                                if (index != 0)
                                                    navController.navigate(item.route)
                                                navViewModel.selectedItemIndex.value = index
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = item.title,
                                                fontFamily = JosefinSans
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.contentDescription
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        Navigation(
                            navigationViewModel = navViewModel,
                            paddingValues = paddingValues,
                            navController = navController,
                            themeViewModel = themeViewModel,
                            defaultScreen = ScreenConfiguration.HomeScreen.route
                        )
                    }
                }
            }
        }
    }
}