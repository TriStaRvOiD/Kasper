package com.tristarvoid.vanguard.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.domain.use_cases.SplashScreenViewModel
import com.tristarvoid.vanguard.presentation.navigation.DrawerBody
import com.tristarvoid.vanguard.presentation.navigation.Navigation
import com.tristarvoid.vanguard.presentation.ui.theme.VanguardTheme
import com.tristarvoid.vanguard.util.MenuItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var splashViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }
            VanguardTheme(
                dynamicColor = false
            ) {
                val screen by splashViewModel.startDestination
                Display(screen)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Display(
    screen: String
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
        Surface {
            Navigation(navController, navViewModel, drawerState, scope, screen)
        }
    }
}