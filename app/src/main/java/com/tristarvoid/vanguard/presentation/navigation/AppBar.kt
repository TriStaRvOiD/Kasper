package com.tristarvoid.vanguard.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.presentation.ui.theme.JosefinSans
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
