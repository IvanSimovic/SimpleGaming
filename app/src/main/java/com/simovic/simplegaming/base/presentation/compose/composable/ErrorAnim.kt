package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simovic.simplegaming.R

@Composable
fun ErrorAnim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LabeledAnimation(R.string.common_data_not_found, R.raw.lottie_error_screen)
    }
}

@Preview
@Composable
private fun ErrorAnimPreview() {
    ErrorAnim()
}
