package com.simovic.simplegaming.base.presentation

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.AuthStateProvider
import com.simovic.simplegaming.base.domain.model.AuthState
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class AppViewModel(
    private val authStateProvider: AuthStateProvider,
) : BaseViewModel<AppUiState, AppAction>(AppUiState.Checking) {
    init {
        viewModelScope.launch {
            authStateProvider.observe().collect { authState ->
                when (authState) {
                    is AuthState.LoggedIn -> sendAction(AppAction.SetAuthenticated(authState.userId))
                    AuthState.LoggedOut -> sendAction(AppAction.SetUnauthenticated)
                }
            }
        }
    }
}
