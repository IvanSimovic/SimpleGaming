package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simovic.simplegaming.base.presentation.ui.AppTheme

@Composable
fun AppPreview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AppTheme {
        Column(modifier = modifier.background(Color.Gray)) {
            Column(modifier = Modifier.padding(10.dp).background(Color.White)) {
                content()
            }
        }
    }
}
