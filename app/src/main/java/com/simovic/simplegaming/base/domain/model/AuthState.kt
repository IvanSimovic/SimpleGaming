package com.simovic.simplegaming.base.domain.model

sealed interface AuthState {
    data class LoggedIn(
        val userId: String,
    ) : AuthState

    data object LoggedOut : AuthState
}
