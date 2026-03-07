package com.simovic.simplegaming.feature.games.data.datasource.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.data.datasource.firestore.model.FavouriteGameFirestoreModel
import com.simovic.simplegaming.feature.games.domain.model.Game
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val COLLECTION_USERS = "users"
private const val COLLECTION_FAVOURITE_GAMES = "favouriteGames"

internal class FavouriteGamesFirestore(
    private val firestore: FirebaseFirestore,
) {
    fun observeFavouriteGames(userId: String): Flow<Result<List<FavouriteGameFirestoreModel>>> =
        callbackFlow {
            val collection =
                firestore
                    .collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_FAVOURITE_GAMES)

            val listener =
                collection.addSnapshotListener { snapshot, error ->
                    when {
                        error != null -> trySend(Result.Failure(error))
                        snapshot == null -> trySend(Result.Failure())
                        else -> trySend(Result.Success(snapshot.documents.mapNotNull(::parseDocument)))
                    }
                }

            awaitClose { listener.remove() }
        }

    private fun parseDocument(doc: DocumentSnapshot): FavouriteGameFirestoreModel? {
        val gameId = doc.getString("gameId") ?: return null
        val name = doc.getString("name") ?: return null
        return FavouriteGameFirestoreModel(
            gameId = gameId,
            name = name,
            imageUrl = doc.getString("imageUrl") ?: "",
            addedAt = doc.getTimestamp("addedAt")?.toDate()?.time ?: 0L,
        )
    }

    suspend fun getFavouriteGameIds(userId: String): Set<String> =
        firestore
            .collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_FAVOURITE_GAMES)
            .get()
            .await()
            .documents
            .mapNotNull { it.getString("gameId") }
            .toSet()

    suspend fun addFavouriteGame(
        userId: String,
        game: Game,
    ) {
        val data =
            mapOf(
                "gameId" to game.id,
                "name" to game.name,
                "imageUrl" to game.imageUrl,
                "addedAt" to FieldValue.serverTimestamp(),
            )
        firestore
            .collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_FAVOURITE_GAMES)
            .document(game.id)
            .set(data)
            .await()
    }

    suspend fun removeFavouriteGame(
        userId: String,
        gameId: String,
    ) {
        firestore
            .collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_FAVOURITE_GAMES)
            .document(gameId)
            .delete()
            .await()
    }
}
