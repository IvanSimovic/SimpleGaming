package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import kotlinx.coroutines.launch

internal class FavouriteGamesViewModel(
    private val getFavouriteGames: GetFavouriteGamesUseCase,
) : BaseViewModel<FavouriteGamesUiState, FavouriteGamesAction>(FavouriteGamesUiState.Loading) {
    init {
        viewModelScope.launch {
            getFavouriteGames().collect { result ->
                when (result) {
                    is Result.Success -> sendAction(FavouriteGamesAction.ShowContent(result.value.toUiModels()))
                    is Result.Failure -> sendAction(FavouriteGamesAction.ShowError)
                }
            }
        }
    }

    private fun List<FavouriteGame>.toUiModels() =
        map { game ->
            FavouriteGameUiModel(
                gameId = game.gameId,
                name = game.name,
                imageUrl = game.imageUrl,
            )
        }
}
