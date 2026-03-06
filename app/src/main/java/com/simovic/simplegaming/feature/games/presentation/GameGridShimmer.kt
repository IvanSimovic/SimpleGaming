package com.simovic.simplegaming.feature.games.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.rememberShimmerBrush

private const val SHIMMER_ITEM_COUNT = 6
private const val SHIMMER_TEXT_WIDTH_FRACTION = 0.65f

@Composable
internal fun GameGridShimmer(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = Dimen.screenContentPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        userScrollEnabled = false,
    ) {
        items(SHIMMER_ITEM_COUNT) {
            Column {
                Box(
                    modifier =
                        Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .background(brush),
                )
                Box(
                    modifier =
                        Modifier
                            .padding(top = Dimen.spaceS)
                            .fillMaxWidth(SHIMMER_TEXT_WIDTH_FRACTION)
                            .height(Dimen.textHeight)
                            .background(brush),
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameGridShimmerPreview() {
    AppPreview {
        GameGridShimmer()
    }
}
