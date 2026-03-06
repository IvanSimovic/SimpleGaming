package com.simovic.simplegaming.feature.games.presentation.screen.addgame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.AppTextField
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.PlaceholderImage
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.presentation.GameGridShimmer
import org.koin.androidx.compose.koinViewModel

private const val GAME_CARD_ASPECT_RATIO = 0.75f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: AddGameViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val currentOnNavigateBack by rememberUpdatedState(onNavigateBack)

    LaunchedEffect(uiState) {
        if (uiState == AddGameUiState.Added) {
            currentOnNavigateBack()
        }
    }

    var query by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.add_game_title)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )

        AppTextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                viewModel.onQueryChanged(newQuery)
            },
            label = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = Dimen.screenContentPadding),
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val currentState = uiState) {
                AddGameUiState.Idle -> Unit
                AddGameUiState.Loading -> GameGridShimmer()
                AddGameUiState.Error -> ErrorAnim()
                AddGameUiState.Empty ->
                    Text(
                        text = stringResource(R.string.common_data_not_found),
                        style = AppTheme.typo.body1,
                        color = AppTheme.color.textMuted,
                    )
                AddGameUiState.Added -> Unit
                is AddGameUiState.Content ->
                    SearchResultGrid(
                        results = currentState.results,
                        onGameSelect = viewModel::addGame,
                    )
            }
        }
    }
}

@Composable
private fun SearchResultGrid(
    results: List<Game>,
    onGameSelect: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(horizontal = Dimen.screenContentPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceM),
    ) {
        items(results, key = { it.id }) { game ->
            SearchResultCell(game = game, onClick = { onGameSelect(game) })
        }
    }
}

@Composable
private fun SearchResultCell(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(Dimen.spaceM),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimen.cardElevation),
        modifier = modifier.fillMaxWidth().aspectRatio(GAME_CARD_ASPECT_RATIO),
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

@Preview
@Composable
private fun AddGameContentPreview() {
    AppPreview {
        SearchResultGrid(
            results =
                listOf(
                    Game("1", "Hollow Knight", ""),
                    Game("2", "Hades", ""),
                ),
            onGameSelect = {},
        )
    }
}
