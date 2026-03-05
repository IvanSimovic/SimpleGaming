package com.simovic.simplegaming.base.presentation

import com.simovic.simplegaming.base.presentation.viewmodel.BaseState

sealed interface AppUiState : BaseState {
    data object Checking : AppUiState

    data object Unauthenticated : AppUiState

    data class Authenticated(
        val userId: String,
    ) : AppUiState
}
