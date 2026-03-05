package com.simovic.simplegaming.base.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.auth.presentation.screen.login.LoginScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppScreen() {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    AppTheme {
        when (uiState) {
            AppUiState.Checking -> Unit
            AppUiState.Unauthenticated -> LoginScreen()
            is AppUiState.Authenticated -> MainShowcaseScreen()
        }
    }
}
