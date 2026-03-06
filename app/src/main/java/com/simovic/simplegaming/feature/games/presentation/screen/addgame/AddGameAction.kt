package com.simovic.simplegaming.feature.games.presentation.screen.addgame

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction
import com.simovic.simplegaming.feature.games.domain.model.Game

internal sealed interface AddGameAction : BaseAction<AddGameUiState> {
    data object ShowIdle : AddGameAction {
        override fun reduce(state: AddGameUiState) = AddGameUiState.Idle
    }

    data object ShowLoading : AddGameAction {
        override fun reduce(state: AddGameUiState) = AddGameUiState.Loading
    }

    data class ShowContent(
        val results: List<Game>,
    ) : AddGameAction {
        override fun reduce(state: AddGameUiState): AddGameUiState =
            if (results.isEmpty()) AddGameUiState.Empty else AddGameUiState.Content(results)
    }

    data object ShowError : AddGameAction {
        override fun reduce(state: AddGameUiState) = AddGameUiState.Error
    }

    data object ShowAdded : AddGameAction {
        override fun reduce(state: AddGameUiState) = AddGameUiState.Added
    }
}
