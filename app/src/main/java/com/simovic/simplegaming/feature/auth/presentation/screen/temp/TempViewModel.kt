package com.simovic.simplegaming.feature.auth.presentation.screen.temp

import androidx.lifecycle.ViewModel
import com.simovic.simplegaming.feature.auth.domain.usecase.SignOutUseCase

internal class TempViewModel(
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    fun signOut() = signOutUseCase()
}
