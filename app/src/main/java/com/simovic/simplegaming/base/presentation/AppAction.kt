package com.simovic.simplegaming.base.presentation

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

sealed interface AppAction : BaseAction<AppUiState> {
    data object SetUnauthenticated : AppAction {
        override fun reduce(state: AppUiState) = AppUiState.Unauthenticated
    }

    data class SetAuthenticated(
        val userId: String,
    ) : AppAction {
        override fun reduce(state: AppUiState) = AppUiState.Authenticated(userId)
    }
}
