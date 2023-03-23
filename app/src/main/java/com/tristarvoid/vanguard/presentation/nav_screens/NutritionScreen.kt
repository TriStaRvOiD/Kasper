package com.tristarvoid.vanguard.presentation.nav_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tristarvoid.vanguard.R
import com.tristarvoid.vanguard.domain.use_cases.NavViewModel
import com.tristarvoid.vanguard.presentation.navigation.AppBar
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nutrition(
    navControl: NavHostController,
    navViewModel: NavViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieLoader(R.raw.food)
//        Text(
//            modifier = Modifier.padding(top = padding.calculateTopPadding()),
//            text = "Nutrition",
//            textAlign = TextAlign.Center
//        )
            }
        }
    }
}