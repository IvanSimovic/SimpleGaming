package com.simovic.simplegaming.base.domain

import com.simovic.simplegaming.base.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthStateProvider {
    fun observe(): Flow<AuthState>
}
