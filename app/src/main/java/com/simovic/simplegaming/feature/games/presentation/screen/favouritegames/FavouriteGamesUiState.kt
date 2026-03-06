package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface FavouriteGamesUiState : BaseState {
    @Immutable data object Loading : FavouriteGamesUiState

    @Immutable data object Empty : FavouriteGamesUiState

    @Immutable data object Error : FavouriteGamesUiState

    @Immutable data class Content(
        val games: List<FavouriteGameUiModel>,
    ) : FavouriteGamesUiState
}
