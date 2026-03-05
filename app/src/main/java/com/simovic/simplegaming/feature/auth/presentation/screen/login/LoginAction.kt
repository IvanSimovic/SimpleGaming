package com.simovic.simplegaming.feature.auth.presentation.screen.login

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface LoginAction : BaseAction<LoginUiState> {
    data object Init : LoginAction {
        override fun reduce(state: LoginUiState) = LoginUiState.Idle
    }

    data object StartLoading : LoginAction {
        override fun reduce(state: LoginUiState) = LoginUiState.Loading
    }

    data class ShowError(
        val message: String,
    ) : LoginAction {
        override fun reduce(state: LoginUiState) = LoginUiState.Error(message)
    }
}
