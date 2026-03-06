package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.simovic.simplegaming.base.presentation.ui.AppTheme

private const val SHIMMER_DURATION_MS = 1000
private const val SHIMMER_SWEEP_WIDTH = 600f
private const val SHIMMER_TRAVEL_DISTANCE = 1600f

@Composable
fun rememberShimmerBrush(): Brush {
    val baseColor = AppTheme.color.divider
    val highlightColor = AppTheme.color.surfaceHigh

    val shimmerColors =
        listOf(
            baseColor,
            highlightColor,
            baseColor,
        )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = -SHIMMER_SWEEP_WIDTH,
        targetValue = SHIMMER_TRAVEL_DISTANCE,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = SHIMMER_DURATION_MS, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "shimmer_offset",
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = offset, y = 0f),
        end = Offset(x = offset + SHIMMER_SWEEP_WIDTH, y = 0f),
    )
}
