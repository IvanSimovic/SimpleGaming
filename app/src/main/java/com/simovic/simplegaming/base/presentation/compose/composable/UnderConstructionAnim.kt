package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simovic.simplegaming.R

@Composable
fun UnderConstructionAnim() {
    LabeledAnimation(R.string.common_under_construction, R.raw.lottie_building_screen)
}

@Preview
@Composable
private fun UnderConstructionAnimPreview() {
    UnderConstructionAnim()
}
