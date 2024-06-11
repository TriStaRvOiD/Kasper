/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tristarvoid.kasper.R
import com.tristarvoid.kasper.navigation.presentation.Navigation
import com.tristarvoid.kasper.navigation.presentation.view_models.NavigationViewModel
import com.tristarvoid.kasper.theme.presentation.view_models.ThemeViewModel
import com.tristarvoid.kasper.theme.presentation.JosefinSans
import com.tristarvoid.kasper.theme.presentation.KasperTheme
import com.tristarvoid.kasper.core.presentation.utils.BottomNavigationItem
import com.tristarvoid.kasper.core.presentation.utils.ScreenConfiguration
import com.tristarvoid.kasper.core.presentation.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition { keepSplash }
        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val isDynamicEnabled by themeViewModel.dynamicEnabled.collectAsStateWithLifecycle()
            val themePref by themeViewModel.themePref.collectAsStateWithLifecycle()

            LaunchedEffect(themePref, isDynamicEnabled) {
                keepSplash = themePref == null || isDynamicEnabled == null
            }
            if (themePref != null && isDynamicEnabled != null) {
                KasperTheme(
                    darkTheme = if (themePref == ThemeMode.SYSTEM.toString()) isSystemInDarkTheme() else if (themePref == ThemeMode.DARK.toString()) true else false,
                    dynamicColor = isDynamicEnabled!!
                ) {
                    val navController = rememberNavController()
                    val navViewModel: NavigationViewModel = viewModel()
                    val bottomNavigationItems = listOf(
                        BottomNavigationItem(
                            title = "Summary",
                            route = ScreenConfiguration.SummaryScreen.route,
                            contentDescription = "Go to summary screen",
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.graph_svgrepo_com),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.graph_svgrepo_com)
                        ),
                        BottomNavigationItem(
                            title = "Home",
                            route = ScreenConfiguration.HomeScreen.route,
                            contentDescription = "Go to home screen",
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.home_svgrepo_com),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.home_svgrepo_com)
                        ),
                        BottomNavigationItem(
                            title = "Map",
                            route = ScreenConfiguration.MapScreen.route,
                            contentDescription = "Go to map screen",
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.map_svgrepo_com),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.map_svgrepo_com)
                        )
                    )

                    val selectedItemIndex by navViewModel.selectedItemIndex.collectAsStateWithLifecycle()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    LaunchedEffect(navBackStackEntry) {
                        if (navBackStackEntry != null) {
                            when (navBackStackEntry!!.destination.route) {
                                ScreenConfiguration.SummaryScreen.route -> {
                                    navViewModel.setSelectedItemIndex(0)
                                }

                                ScreenConfiguration.HomeScreen.route -> {
                                    navViewModel.setSelectedItemIndex(1)
                                }

                                ScreenConfiguration.MapScreen.route -> {
                                    navViewModel.setSelectedItemIndex(2)
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    tonalElevation = 0.dp
                                ) {
                                    bottomNavigationItems.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedItemIndex == index,
                                            onClick = {
                                                if (navViewModel.selectedItemIndex.value != index) {
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                    navViewModel.setSelectedItemIndex(index)
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
}