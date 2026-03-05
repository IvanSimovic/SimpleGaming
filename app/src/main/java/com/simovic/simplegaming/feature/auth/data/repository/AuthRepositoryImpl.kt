package com.simovic.simplegaming.feature.auth.data.repository

import com.simovic.simplegaming.base.domain.model.AuthState
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.auth.data.datasource.firebase.FirebaseAuthDataSource
import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

internal class AuthRepositoryImpl(
    private val dataSource: FirebaseAuthDataSource,
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = dataSource.observeAuthState()

    override suspend fun signIn(
        email: String,
        password: String,
    ): Result<Unit> =
        try {
            dataSource.signIn(email, password)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override fun getCurrentUserId(): String? = dataSource.getCurrentUserId()

    override fun signOut() = dataSource.signOut()
}
