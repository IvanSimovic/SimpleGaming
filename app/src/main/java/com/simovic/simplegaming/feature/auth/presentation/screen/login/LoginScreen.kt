package com.simovic.simplegaming.feature.auth.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppButton
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.AppTextField
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("test@gmail.com") }
    var password by remember { mutableStateOf("test21") }
    val passwordFocusRequester = remember { FocusRequester() }

    LoginScreenContent(
        uiState = uiState,
        email = email,
        password = password,
        passwordFocusRequester = passwordFocusRequester,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onSignIn = { viewModel.signIn(email, password) },
        modifier = modifier,
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginUiState,
    email: String,
    password: String,
    passwordFocusRequester: FocusRequester,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = Dimen.screenContentPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() },
                ),
        )

        Spacer(Modifier.height(Dimen.spaceL))

        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            modifier = Modifier.focusRequester(passwordFocusRequester),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions = KeyboardActions(onDone = { onSignIn() }),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(Modifier.height(Dimen.spaceXL))

        AppButton(
            text = "Sign In",
            onClick = onSignIn,
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState is LoginUiState.Loading,
            enabled = uiState !is LoginUiState.Loading,
        )

        if (uiState is LoginUiState.Error) {
            Spacer(Modifier.height(Dimen.spaceM))
            Text(
                text = uiState.message,
                style = AppTheme.typo.body2,
                color = AppTheme.color.error,
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppPreview {
        LoginScreenContent(
            uiState = LoginUiState.Idle,
            email = "test@gmail.com",
            password = "test21",
            passwordFocusRequester = FocusRequester(),
            onEmailChange = {},
            onPasswordChange = {},
            onSignIn = {},
        )
    }
}
