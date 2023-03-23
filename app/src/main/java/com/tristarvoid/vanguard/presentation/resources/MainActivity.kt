package com.tristarvoid.vanguard.presentation.resources

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.data.use_cases.navigation.MenuItem
import com.tristarvoid.vanguard.domain.use_cases.navigation.NavViewModel
import com.tristarvoid.vanguard.presentation.resources.use_cases.navigation.Navigation
import com.tristarvoid.vanguard.presentation.resources.use_cases.navigation.ScreenConfiguration
import com.tristarvoid.vanguard.presentation.ui.theme.JosefinSans
import com.tristarvoid.vanguard.presentation.ui.theme.VanguardTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
        }
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }
            VanguardTheme(
                dynamicColor = false
            ) {
                Display()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Display(
) {
    val navViewModel = viewModel<NavViewModel>()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerBody(
                navControl = navController,
                drawerState = drawerState,
                scope = scope,
                navViewModel = navViewModel,
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "home",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.home)
                    ),
                    MenuItem(
                        id = "workouts",
                        title = "workouts",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.exercise)
                    ),
                    MenuItem(
                        id = "decisions",
                        title = "decisions",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.balance)
                    ),
                    MenuItem(
                        id = "reminders",
                        title = "reminders",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.reminders)
                    ),
                    MenuItem(
                        id = "food",
                        title = "nutrition",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.nutrition)
                    ),
                    MenuItem(
                        id = "water",
                        title = "h2o",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.water)
                    ),
                    MenuItem(
                        id = "settings",
                        title = "settings",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.settings)
                    ),
                    MenuItem(
                        id = "privacy",
                        title = "privacy",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.privacy)
                    ),
                    MenuItem(
                        id = "about",
                        title = "about",
                        contentDescription = "Go to home screen",
                        icon = painterResource(id = R.drawable.info)
                    )
                )
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(navController, drawerState, scope, navViewModel)
            }
        ) {
            Navigation(it, navController, navViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navControl: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navViewModel: NavViewModel
) {
    val heading = remember {
        navViewModel.heading
    }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                modifier = Modifier,
                text = heading.value,
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
