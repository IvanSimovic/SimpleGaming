@file:Suppress("MagicNumber")

package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.FullScreenImageViewer
import com.simovic.simplegaming.base.presentation.compose.composable.rememberShimmerBrush
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import org.koin.androidx.compose.koinViewModel

private val ScrimTop =
    Brush.verticalGradient(
        0.0f to Color.Black.copy(alpha = 0.55f),
        0.2f to Color.Transparent,
    )

private val ScrimBottom =
    Brush.verticalGradient(
        0.35f to Color.Transparent,
        1.0f to Color.Black.copy(alpha = 0.88f),
    )

@Composable
internal fun ReelsScreen(
    modifier: Modifier = Modifier,
    contentBottomPadding: Dp = 0.dp,
) {
    val viewModel: ReelsViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            ReelsUiState.Loading -> {
                FullScreenShimmer()
            }
            ReelsUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ErrorAnim() }
            }
            is ReelsUiState.Content -> {
                ReelsPager(state = state, viewModel = viewModel, contentBottomPadding = contentBottomPadding)
            }
        }
    }
}

@Composable
private fun ReelsPager(
    state: ReelsUiState.Content,
    viewModel: ReelsViewModel,
    contentBottomPadding: Dp,
) {
    val pagerState = rememberPagerState { state.pages.size }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { index ->
            viewModel.onPageVisible(index)
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { index ->
        when (val page = state.pages.getOrElse(index) { ReelPageState.Loading }) {
            ReelPageState.Loading -> {
                FullScreenShimmer()
            }
            ReelPageState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ErrorAnim() }
            }
            is ReelPageState.Loaded -> {
                ReelPage(
                    game = page.game,
                    isFavourite = page.game.id in state.favouriteIds,
                    onToggleFavourite = { viewModel.toggleFavourite(page.game) },
                    contentBottomPadding = contentBottomPadding,
                )
            }
        }
    }
}

@Composable
private fun ReelPage(
    game: ReelGame,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit,
    contentBottomPadding: Dp,
) {
    var selectedScreenshotIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = game.heroImage,
            contentDescription = game.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(ScrimTop),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(ScrimBottom),
        )

        IconButton(
            onClick = onToggleFavourite,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(Dimen.spaceM)
                    .background(Color.Black.copy(alpha = 0.35f), shape = CircleShape),
        ) {
            Icon(
                imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(R.string.reels_toggle_favourite),
                tint = if (isFavourite) Color(0xFFE53935) else Color.White,
                modifier = Modifier.size(28.dp),
            )
        }

        GameInfoColumn(
            game = game,
            onScreenshotClick = { index -> selectedScreenshotIndex = index },
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = Dimen.screenContentPadding)
                    .padding(bottom = contentBottomPadding + Dimen.spaceL),
        )

        selectedScreenshotIndex?.let { index ->
            FullScreenImageViewer(
                images = game.screenshots,
                initialIndex = index,
                onDismiss = { selectedScreenshotIndex = null },
            )
        }
    }
}

@Composable
private fun GameInfoColumn(
    game: ReelGame,
    onScreenshotClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        MetacriticRatingRow(metacritic = game.metacritic, rating = game.rating)

        Text(
            text = game.name,
            style = AppTheme.typo.head4,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        if (game.genres.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimen.spaceS)) {
                game.genres.take(3).forEach { genre -> Chip(label = genre) }
            }
        }

        if (game.description.isNotBlank()) {
            Text(
                text = game.description,
                style = AppTheme.typo.body3,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (game.screenshots.isNotEmpty()) {
            ScreenshotRow(screenshots = game.screenshots, onImageClick = onScreenshotClick)
        }

        PlatformRow(playtime = game.playtime, platforms = game.platforms)
    }
}

@Composable
private fun MetacriticRatingRow(
    metacritic: Int?,
    rating: Float,
) {
    if (metacritic == null && rating <= 0f) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        metacritic?.let { MetacriticBadge(score = it) }
        if (rating > 0f) {
            Text(
                text = "★ ${"%.1f".format(rating)}",
                style = AppTheme.typo.body2,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun ScreenshotRow(
    screenshots: List<String>,
    onImageClick: (Int) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        contentPadding = PaddingValues(0.dp),
    ) {
        items(screenshots.size) { index ->
            AsyncImage(
                model = screenshots[index],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(width = 120.dp, height = 68.dp)
                        .clip(RoundedCornerShape(Dimen.spaceM))
                        .clickable { onImageClick(index) },
            )
        }
    }
}

@Composable
private fun PlatformRow(
    playtime: Int,
    platforms: List<String>,
) {
    if (playtime <= 0 && platforms.isEmpty()) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        if (playtime > 0) {
            Text(
                text = "🕹 ${playtime}h",
                style = AppTheme.typo.body3,
                color = Color.White.copy(alpha = 0.75f),
            )
        }
        platforms.take(4).forEach { platform -> Chip(label = platform) }
    }
}

@Composable
private fun MetacriticBadge(score: Int) {
    val color =
        when {
            score >= 75 -> Color(0xFF4CAF50)
            score >= 50 -> Color(0xFFFFC107)
            else -> Color(0xFFF44336)
        }
    Box(
        modifier =
            Modifier
                .background(color, RoundedCornerShape(4.dp))
                .padding(horizontal = Dimen.spaceM, vertical = Dimen.spaceS),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = score.toString(),
            style = AppTheme.typo.body6,
            color = Color.White,
        )
    }
}

@Composable
private fun Chip(label: String) {
    Box(
        modifier =
            Modifier
                .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(Dimen.spaceXL))
                .padding(horizontal = Dimen.spaceM, vertical = Dimen.spaceS),
    ) {
        Text(
            text = label,
            style = AppTheme.typo.body3,
            color = Color.White,
        )
    }
}

@Composable
private fun FullScreenShimmer() {
    val brush = rememberShimmerBrush()
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(brush),
    )
}
