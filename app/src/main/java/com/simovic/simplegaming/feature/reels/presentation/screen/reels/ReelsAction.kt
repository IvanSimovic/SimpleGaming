package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface ReelsAction : BaseAction<ReelsUiState> {
    data object ShowError : ReelsAction {
        override fun reduce(state: ReelsUiState) = ReelsUiState.Error
    }

    data class ShowPages(
        val ids: List<String>,
    ) : ReelsAction {
        override fun reduce(state: ReelsUiState): ReelsUiState =
            ReelsUiState.Content(
                pages = ids.map { ReelPageState.Loading },
                favouriteIds = (state as? ReelsUiState.Content)?.favouriteIds ?: emptySet(),
            )
    }

    data class UpdatePage(
        val index: Int,
        val pageState: ReelPageState,
    ) : ReelsAction {
        override fun reduce(state: ReelsUiState): ReelsUiState {
            if (state !is ReelsUiState.Content) return state
            val updated = state.pages.toMutableList().also { it[index] = pageState }
            return state.copy(pages = updated)
        }
    }

    data class UpdateFavouriteIds(
        val ids: Set<String>,
    ) : ReelsAction {
        override fun reduce(state: ReelsUiState): ReelsUiState =
            if (state is ReelsUiState.Content) state.copy(favouriteIds = ids) else state
    }
}
