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
private const val FIELD_GAME_ID = "gameId"
private const val FIELD_NAME = "name"
private const val FIELD_IMAGE_URL = "imageUrl"
private const val FIELD_ADDED_AT = "addedAt"

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
        val gameId = doc.getString(FIELD_GAME_ID) ?: return null
        val name = doc.getString(FIELD_NAME) ?: return null
        return FavouriteGameFirestoreModel(
            gameId = gameId,
            name = name,
            imageUrl = doc.getString(FIELD_IMAGE_URL) ?: "",
            addedAt = doc.getTimestamp(FIELD_ADDED_AT)?.toDate()?.time ?: 0L,
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
            .mapNotNull { it.getString(FIELD_GAME_ID) }
            .toSet()

    suspend fun addFavouriteGame(
        userId: String,
        game: Game,
    ) {
        val data =
            mapOf(
                FIELD_GAME_ID to game.id,
                FIELD_NAME to game.name,
                FIELD_IMAGE_URL to game.imageUrl,
                FIELD_ADDED_AT to FieldValue.serverTimestamp(),
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
