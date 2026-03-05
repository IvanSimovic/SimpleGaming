package com.simovic.simplegaming.feature.auth.domain.repository

import com.simovic.simplegaming.base.domain.model.AuthState
import com.simovic.simplegaming.base.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>

    suspend fun signIn(
        email: String,
        password: String,
    ): Result<Unit>

    fun getCurrentUserId(): String?

    fun signOut()
}
