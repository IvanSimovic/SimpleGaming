package com.simovic.simplegaming.feature.auth.data.datasource.firebase

import com.google.firebase.auth.FirebaseAuth
import com.simovic.simplegaming.base.domain.model.AuthState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthDataSource(
    private val firebaseAuth: FirebaseAuth,
) {
    fun observeAuthState(): Flow<AuthState> =
        callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    val user = auth.currentUser
                    trySend(
                        if (user != null) AuthState.LoggedIn(user.uid) else AuthState.LoggedOut,
                    )
                }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    suspend fun signIn(
        email: String,
        password: String,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    fun signOut() = firebaseAuth.signOut()
}
