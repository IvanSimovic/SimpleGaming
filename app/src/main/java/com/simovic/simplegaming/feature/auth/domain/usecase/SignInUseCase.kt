package com.simovic.simplegaming.feature.auth.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository

class SignInUseCase(
    private val repo: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<Unit> = repo.signIn(email, password)
}
