package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.usecase.AddFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.RemoveFavouriteGameUseCase
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameIdsUseCase
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameUseCase
import kotlinx.coroutines.launch

internal class ReelsViewModel(
    private val getReelGameIds: GetReelGameIdsUseCase,
    private val getReelGame: GetReelGameUseCase,
    private val getFavouriteGames: GetFavouriteGamesUseCase,
    private val addFavouriteGame: AddFavouriteGameUseCase,
    private val removeFavouriteGame: RemoveFavouriteGameUseCase,
) : BaseViewModel<ReelsUiState, ReelsAction>(ReelsUiState.Loading) {
    // Private ViewModel state — not part of UI state since the screen doesn't render IDs directly
    private var reelIds: List<String> = emptyList()
    private val loadingPages = mutableSetOf<Int>()

    init {
        collectFavourites()
        loadReelIds()
    }

    private fun collectFavourites() {
        viewModelScope.launch {
            getFavouriteGames().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val ids = result.value.map { it.gameId }.toSet()
                        sendAction(ReelsAction.UpdateFavouriteIds(ids))
                    }
                    is Result.Failure -> {
                        // Favourites unavailable — heart states remain as-is
                    }
                }
            }
        }
    }

    private fun loadReelIds() {
        viewModelScope.launch {
            when (val result = getReelGameIds()) {
                is Result.Success -> {
                    reelIds = result.value
                    sendAction(ReelsAction.ShowPages(reelIds))
                    triggerLoad(0)
                    triggerLoad(1)
                }
                is Result.Failure -> {
                    sendAction(ReelsAction.ShowError)
                }
            }
        }
    }

    fun onPageVisible(index: Int) {
        val content = uiStateFlow.value as? ReelsUiState.Content ?: return
        if (content.pages.getOrNull(index) is ReelPageState.Loading) {
            triggerLoad(index)
        }
        if (content.pages.getOrNull(index + 1) is ReelPageState.Loading) {
            triggerLoad(index + 1)
        }
    }

    fun toggleFavourite(game: ReelGame) {
        val content = uiStateFlow.value as? ReelsUiState.Content ?: return
        viewModelScope.launch {
            if (game.id in content.favouriteIds) {
                removeFavouriteGame(game.id)
            } else {
                addFavouriteGame(Game(id = game.id, name = game.name, imageUrl = game.heroImage))
            }
        }
    }

    private fun triggerLoad(index: Int) {
        val id = reelIds.getOrNull(index) ?: return
        if (index in loadingPages) return
        loadingPages.add(index)
        viewModelScope.launch {
            when (val result = getReelGame(id)) {
                is Result.Success -> sendAction(ReelsAction.UpdatePage(index, ReelPageState.Loaded(result.value)))
                is Result.Failure -> sendAction(ReelsAction.UpdatePage(index, ReelPageState.Error))
            }
            loadingPages.remove(index)
        }
    }
}
