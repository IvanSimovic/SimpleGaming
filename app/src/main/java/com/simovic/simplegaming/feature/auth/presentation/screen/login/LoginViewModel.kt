package com.simovic.simplegaming.feature.auth.presentation.screen.login

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.auth.domain.usecase.SignInUseCase
import kotlinx.coroutines.launch

internal class LoginViewModel(
    private val signInUseCase: SignInUseCase,
) : BaseViewModel<LoginUiState, LoginAction>(LoginUiState.Idle) {
    fun signIn(
        email: String,
        password: String,
    ) {
        if (email.isBlank()) {
            sendAction(LoginAction.ShowError("Email is required"))
            return
        }
        if (password.isBlank()) {
            sendAction(LoginAction.ShowError("Password is required"))
            return
        }
        viewModelScope.launch {
            sendAction(LoginAction.StartLoading)
            when (val result = signInUseCase(email, password)) {
                is Result.Success -> sendAction(LoginAction.Init)
                is Result.Failure ->
                    sendAction(
                        LoginAction.ShowError(result.throwable?.message ?: "Sign in failed"),
                    )
            }
        }
    }
}
