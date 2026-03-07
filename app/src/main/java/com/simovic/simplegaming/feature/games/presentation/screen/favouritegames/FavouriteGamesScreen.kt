@file:Suppress("MagicNumber")

package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.PlaceholderImage
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.games.presentation.GameGridShimmer
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

private const val GAME_CARD_ASPECT_RATIO = 0.75f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteGamesScreen(modifier: Modifier = Modifier) {
    val viewModel: FavouriteGamesViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    var selectedGameId by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.favourite_games_title)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val currentState = uiState) {
                FavouriteGamesUiState.Loading -> GameGridShimmer()
                FavouriteGamesUiState.Error -> ErrorAnim()
                FavouriteGamesUiState.Empty ->
                    Text(
                        text = stringResource(R.string.favourite_games_empty),
                        style = AppTheme.typo.body1,
                        color = AppTheme.color.textMuted,
                    )
                is FavouriteGamesUiState.Content ->
                    GameGrid(
                        games = currentState.games,
                        selectedGameId = selectedGameId,
                        onLongClick = { selectedGameId = it },
                        onDelete = { gameId ->
                            viewModel.removeGame(gameId)
                            selectedGameId = null
                        },
                        onCancel = { selectedGameId = null },
                    )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameGrid(
    games: List<FavouriteGameUiModel>,
    selectedGameId: String?,
    onLongClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(horizontal = Dimen.screenContentPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        items(games, key = { it.gameId }) { game ->
            GameCell(
                game = game,
                isSelected = game.gameId == selectedGameId,
                isOtherSelected = selectedGameId != null && game.gameId != selectedGameId,
                onLongClick = { onLongClick(game.gameId) },
                onDelete = { onDelete(game.gameId) },
                onCancel = onCancel,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameCell(
    game: FavouriteGameUiModel,
    isSelected: Boolean,
    isOtherSelected: Boolean,
    onLongClick: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val offsetX = remember { Animatable(0f) }
    val dimAlpha by animateFloatAsState(
        targetValue = if (isOtherSelected) 0.6f else 0f,
        label = "dim",
    )

    LaunchedEffect(isSelected) {
        if (isSelected) {
            repeat(4) {
                offsetX.animateTo(8f, tween(50))
                offsetX.animateTo(-8f, tween(50))
            }
            offsetX.animateTo(0f, tween(50))
        } else {
            offsetX.snapTo(0f)
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(GAME_CARD_ASPECT_RATIO)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .combinedClickable(
                    onLongClick = onLongClick,
                    onClick = {},
                ),
    ) {
        GameCardContent(game = game)

        if (dimAlpha > 0f) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = dimAlpha), RoundedCornerShape(Dimen.spaceM)),
            )
        }

        if (isSelected) {
            DeleteOverlay(onDelete = onDelete, onCancel = onCancel)
        }
    }
}

@Composable
private fun GameCardContent(
    game: FavouriteGameUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(Dimen.spaceM),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimen.cardElevation),
        modifier = modifier.fillMaxSize(),
    ) {
        if (game.imageUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize().background(AppTheme.color.surfaceHigh),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = game.name,
                    style = AppTheme.typo.body2,
                    color = AppTheme.color.textMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(Dimen.spaceM),
                )
            }
        } else {
            PlaceholderImage(
                url = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun DeleteOverlay(
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.favourite_remove).uppercase(),
            style = AppTheme.typo.body2,
            color = Color(0xFFEF5350),
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(Dimen.spaceXL))
                    .clickable(onClick = onDelete)
                    .padding(horizontal = Dimen.spaceL, vertical = Dimen.spaceM),
        )
        Text(
            text = stringResource(R.string.favourite_cancel).uppercase(),
            style = AppTheme.typo.body3,
            color = Color.White,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Dimen.spaceM)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(Dimen.spaceXL))
                    .clickable(onClick = onCancel)
                    .padding(horizontal = Dimen.spaceL, vertical = Dimen.spaceM),
        )
    }
}

@Preview
@Composable
private fun FavouriteGamesContentPreview() {
    AppPreview {
        GameGrid(
            games =
                listOf(
                    FavouriteGameUiModel("1", "Hollow Knight", ""),
                    FavouriteGameUiModel("2", "Hades", ""),
                    FavouriteGameUiModel("3", "Celeste", ""),
                    FavouriteGameUiModel("4", "Dead Cells", ""),
                ),
            selectedGameId = null,
            onLongClick = {},
            onDelete = {},
            onCancel = {},
        )
    }
}
