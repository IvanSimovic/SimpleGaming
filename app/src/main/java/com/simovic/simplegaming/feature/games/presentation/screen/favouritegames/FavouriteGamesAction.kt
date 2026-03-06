package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface FavouriteGamesAction : BaseAction<FavouriteGamesUiState> {
    data class ShowContent(
        val games: List<FavouriteGameUiModel>,
    ) : FavouriteGamesAction {
        override fun reduce(state: FavouriteGamesUiState): FavouriteGamesUiState =
            if (games.isEmpty()) FavouriteGamesUiState.Empty else FavouriteGamesUiState.Content(games)
    }

    data object ShowError : FavouriteGamesAction {
        override fun reduce(state: FavouriteGamesUiState) = FavouriteGamesUiState.Error
    }
}
