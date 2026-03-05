package com.simovic.simplegaming.feature.auth.domain.usecase

import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository

class SignOutUseCase(
    private val repo: AuthRepository,
) {
    operator fun invoke() = repo.signOut()
}
