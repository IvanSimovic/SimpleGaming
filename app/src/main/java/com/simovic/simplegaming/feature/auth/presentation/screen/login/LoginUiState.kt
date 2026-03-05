package com.simovic.simplegaming.feature.auth.presentation.screen.login

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface LoginUiState : BaseState {
    @Immutable
    data object Idle : LoginUiState

    @Immutable
    data object Loading : LoginUiState

    @Immutable
    data class Error(
        val message: String,
    ) : LoginUiState
}
