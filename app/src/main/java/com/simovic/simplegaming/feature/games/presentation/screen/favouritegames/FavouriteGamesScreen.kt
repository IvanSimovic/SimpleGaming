package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.PlaceholderImage
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.games.presentation.GameGridShimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteGamesScreen(modifier: Modifier = Modifier) {
    val viewModel: FavouriteGamesViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

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
                is FavouriteGamesUiState.Content -> GameGrid(currentState.games)
            }
        }
    }
}

@Composable
private fun GameGrid(
    games: List<FavouriteGameUiModel>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(horizontal = Dimen.screenContentPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        items(games, key = { it.gameId }) { game ->
            GameCell(game)
        }
    }
}

@Composable
private fun GameCell(
    game: FavouriteGameUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PlaceholderImage(
            url = game.imageUrl,
            contentDescription = game.name,
            modifier = Modifier.aspectRatio(1f),
        )
        Text(
            text = game.name,
            style = AppTheme.typo.body2,
            color = AppTheme.color.textMain,
            modifier = Modifier.padding(top = Dimen.spaceS),
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
        )
    }
}
