package com.simovic.simplegaming.base.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector

data class FabConfig(
    val icon: ImageVector = Icons.Default.Add,
    val onClick: () -> Unit = {},
    val isVisible: Boolean = true,
)
