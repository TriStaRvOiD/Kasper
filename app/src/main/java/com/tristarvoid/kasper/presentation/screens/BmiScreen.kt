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

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tristarvoid.kasper.domain.HolderViewModel
import com.tristarvoid.kasper.presentation.navigation.MainAppBar
import com.tristarvoid.kasper.presentation.ui.theme.JosefinSans
import com.tristarvoid.kasper.view.Header
import kotlinx.coroutines.CoroutineScope
import kotlin.math.pow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Bmi(
    navControl: NavHostController,
    holderViewModel: HolderViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            MainAppBar(navControl, drawerState, scope, holderViewModel, false)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val weight = remember {
                mutableStateOf("")
            }
            val bmi = remember {
                mutableStateOf(0.0)
            }
            val height = remember {
                mutableStateOf("")
            }
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(5.dp),
                alignment = Alignment.TopStart,
                text = "BMI"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider() //Below header
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Weight in kg : ",
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = weight.value,
                onValueChange = {
                    if (it.length <= 3) weight.value = it
                },
                textStyle = TextStyle(fontFamily = JosefinSans, fontSize = 21.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Height in cm : ",
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = height.value,
                onValueChange = {
                    if (it.length <= 3) height.value = it
                },
                textStyle = TextStyle(fontFamily = JosefinSans, fontSize = 21.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier
                    .padding(16.dp),
                onClick = {
                    keyboardController?.hide()
                    if (weight.value != "" && height.value != "") {
                        try {
                            val one = weight.value.toDouble()
                            val two = height.value.toDouble()
                            val ans = one / (two / 100).pow(2.0)
                            val solution: Double = String
                                .format("%.2f", ans)
                                .toDouble()
                            bmi.value = solution
                        } catch (e: NumberFormatException) {

                        }
                    }
                }
            ) {
                Text(
                    text = "Calculate",
                    fontSize = 21.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontFamily = JosefinSans
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (bmi.value != 0.0) {
                Text(
                    text = "You're BMI is ${bmi.value}",
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "This could potentially mean that you are " + if (bmi.value < 18.5) "Underweight" else if (bmi.value in 18.5..24.9) "Normal weight" else if (bmi.value in 25.0..29.9) "Over weight" else "Obese",
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}