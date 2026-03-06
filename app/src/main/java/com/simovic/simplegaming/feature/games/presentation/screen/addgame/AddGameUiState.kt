package com.simovic.simplegaming.feature.games.presentation.screen.addgame

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState
import com.simovic.simplegaming.feature.games.domain.model.Game

@Immutable
internal sealed interface AddGameUiState : BaseState {
    @Immutable data object Idle : AddGameUiState

    @Immutable data object Loading : AddGameUiState

    @Immutable data object Empty : AddGameUiState

    @Immutable data object Error : AddGameUiState

    @Immutable data object Added : AddGameUiState

    @Immutable data class Content(
        val results: List<Game>,
    ) : AddGameUiState
}
