package com.simovic.simplegaming.feature.auth.domain.usecase

import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository

class GetCurrentUserIdUseCase(
    private val repo: AuthRepository,
) {
    operator fun invoke(): String? = repo.getCurrentUserId()
}
