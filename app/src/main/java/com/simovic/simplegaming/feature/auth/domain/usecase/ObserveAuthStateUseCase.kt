package com.simovic.simplegaming.feature.auth.domain.usecase

import com.simovic.simplegaming.base.domain.AuthStateProvider
import com.simovic.simplegaming.base.domain.model.AuthState
import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val repo: AuthRepository,
) : AuthStateProvider {
    override fun observe(): Flow<AuthState> = repo.observeAuthState()
}
