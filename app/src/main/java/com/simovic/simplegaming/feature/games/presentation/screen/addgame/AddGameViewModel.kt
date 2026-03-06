package com.simovic.simplegaming.feature.games.presentation.screen.addgame

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.usecase.AddFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.SearchGamesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class AddGameViewModel(
    private val searchGames: SearchGamesUseCase,
    private val addFavouriteGame: AddFavouriteGameUseCase,
) : BaseViewModel<AddGameUiState, AddGameAction>(AddGameUiState.Idle) {
    private val query = MutableStateFlow("")

    init {
        query
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .filter { it.isBlank() || it.length >= MIN_QUERY_LENGTH }
            .onEach { q ->
                if (q.isBlank()) {
                    sendAction(AddGameAction.ShowIdle)
                } else {
                    search(q)
                }
            }.launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun addGame(game: Game) {
        viewModelScope.launch {
            when (addFavouriteGame(game)) {
                is Result.Success -> sendAction(AddGameAction.ShowAdded)
                is Result.Failure -> sendAction(AddGameAction.ShowError)
            }
        }
    }

    private suspend fun search(q: String) {
        sendAction(AddGameAction.ShowLoading)
        when (val result = searchGames(q)) {
            is Result.Success -> sendAction(AddGameAction.ShowContent(result.value))
            is Result.Failure -> sendAction(AddGameAction.ShowError)
        }
    }

    companion object {
        private const val DEBOUNCE_MS = 500L
        private const val MIN_QUERY_LENGTH = 2
    }
}
