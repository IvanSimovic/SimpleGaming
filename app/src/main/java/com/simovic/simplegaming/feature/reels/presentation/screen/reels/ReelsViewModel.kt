@file:Suppress("MagicNumber")

package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.usecase.AddFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGameIdsUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.RemoveFavouriteGameUseCase
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameIdsUseCase
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameUseCase
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 20
private const val PREFETCH_THRESHOLD = 5

internal class ReelsViewModel(
    private val getReelGameIds: GetReelGameIdsUseCase,
    private val getFavouriteGameIds: GetFavouriteGameIdsUseCase,
    private val getReelGame: GetReelGameUseCase,
    private val getFavouriteGames: GetFavouriteGamesUseCase,
    private val addFavouriteGame: AddFavouriteGameUseCase,
    private val removeFavouriteGame: RemoveFavouriteGameUseCase,
) : BaseViewModel<ReelsUiState, ReelsAction>(ReelsUiState.Loading) {
    private var reelIds: List<String> = emptyList()
    private val loadingPages = mutableSetOf<Int>()
    private var savedIds: Set<String> = emptySet()
    private var isLoadingMoreIds = false
    private var hasMoreIds = true

    init {
        collectFavourites()
        loadInitial()
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

    private fun loadInitial() {
        viewModelScope.launch {
            savedIds =
                when (val result = getFavouriteGameIds()) {
                    is Result.Success -> result.value
                    is Result.Failure -> emptySet()
                }
            getReelGameIds.reset()
            loadNextPage(isInitial = true)
        }
    }

    private fun loadNextPage(isInitial: Boolean = false) {
        if (isLoadingMoreIds || !hasMoreIds) return
        isLoadingMoreIds = true
        viewModelScope.launch {
            when (val result = getReelGameIds(PAGE_SIZE, savedIds)) {
                is Result.Success -> {
                    val newIds = result.value
                    hasMoreIds = newIds.isNotEmpty()
                    if (isInitial) {
                        reelIds = newIds
                        sendAction(ReelsAction.ShowPages(newIds))
                        triggerLoad(0)
                        triggerLoad(1)
                    } else {
                        val startIndex = reelIds.size
                        reelIds = reelIds + newIds
                        sendAction(ReelsAction.AppendPages(newIds))
                        triggerLoad(startIndex)
                        triggerLoad(startIndex + 1)
                    }
                }
                is Result.Failure -> {
                    if (isInitial) sendAction(ReelsAction.ShowError)
                }
            }
            isLoadingMoreIds = false
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
        if (index >= reelIds.size - PREFETCH_THRESHOLD) {
            loadNextPage()
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
