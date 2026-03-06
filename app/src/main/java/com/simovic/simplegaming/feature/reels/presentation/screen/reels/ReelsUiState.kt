package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame

@Immutable
internal sealed interface ReelsUiState : BaseState {
    @Immutable data object Loading : ReelsUiState

    @Immutable data object Error : ReelsUiState

    @Immutable data class Content(
        val pages: List<ReelPageState>,
        val favouriteIds: Set<String>,
    ) : ReelsUiState
}

@Immutable
internal sealed interface ReelPageState {
    @Immutable data object Loading : ReelPageState

    @Immutable data object Error : ReelPageState

    @Immutable data class Loaded(
        val game: ReelGame,
    ) : ReelPageState
}
