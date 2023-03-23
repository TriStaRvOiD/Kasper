package com.tristarvoid.vanguard.presentation.nav_screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.domain.use_cases.StepsViewModel
import com.tristarvoid.vanguard.presentation.navigation.AppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navControl: NavHostController,
    navViewModel: NavViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val toaster = get<Toast>()
    var showToast by remember { mutableStateOf(false) }

    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }

    if (showToast) {
        toaster.setText("Press back again to exit")
        toaster.duration = Toast.LENGTH_LONG
        toaster.show()
        showToast = false
    }

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }

    Scaffold(
        topBar = {
            AppBar(navControl, drawerState, scope, navViewModel)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            val permissionState =
                rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
            if (permissionState.status == PermissionStatus.Granted) {
                val viewModel: StepsViewModel = getViewModel()
                val steps by viewModel.steps.collectAsState()
                Text(
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    text = steps,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}
