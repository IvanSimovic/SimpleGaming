package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.ui.AppTheme

@Composable
fun LabeledAnimation(
    @StringRes label: Int,
    @RawRes assetResId: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.wrapContentSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .wrapContentSize()
                    .padding(Dimen.spaceXL),
        ) {
            Text(text = stringResource(label), style = AppTheme.typo.body1, color = AppTheme.color.textMain)
            LottieAssetLoader(assetResId)
        }
    }
}

@Composable
fun LottieAssetLoader(
    @RawRes assetResId: Int,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(assetResId))

    LottieAnimation(
        composition,
        modifier = modifier.requiredSize(Dimen.imageSize),
    )
}

@Preview
@Composable
private fun LabeledAnimationPreview() {
    LabeledAnimation(
        label = android.R.string.ok,
        assetResId = com.simovic.simplegaming.R.raw.lottie_building_screen,
    )
}

@Preview
@Composable
private fun LottieAssetLoaderPreview() {
    LottieAssetLoader(
        assetResId = com.simovic.simplegaming.R.raw.lottie_building_screen,
    )
}
