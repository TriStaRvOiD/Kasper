package com.tristarvoid.vanguard.util

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: Painter
)
