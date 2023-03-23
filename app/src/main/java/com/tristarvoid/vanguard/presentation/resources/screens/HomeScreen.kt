package com.tristarvoid.vanguard.presentation.resources.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.tristarvoid.vanguard.domain.use_cases.step_counting.StepsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(
    padding: PaddingValues
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
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
                text = steps,
                textAlign = TextAlign.Center
            )
        }
    }
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}
