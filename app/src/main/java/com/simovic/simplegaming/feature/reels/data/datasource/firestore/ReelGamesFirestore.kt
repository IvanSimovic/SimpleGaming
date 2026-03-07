@file:Suppress("TooGenericExceptionCaught")

package com.simovic.simplegaming.feature.reels.data.datasource.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.simovic.simplegaming.base.domain.result.Result
import kotlinx.coroutines.tasks.await

private const val COLLECTION_GAME_STATS = "gameStats"
private const val FIELD_SAVE_COUNT = "saveCount"

internal class ReelGamesFirestore(
    private val firestore: FirebaseFirestore,
) {
    private var lastDocument: DocumentSnapshot? = null

    fun reset() {
        lastDocument = null
    }

    suspend fun getNextPage(pageSize: Int): Result<List<String>> =
        try {
            val query =
                firestore
                    .collection(COLLECTION_GAME_STATS)
                    .orderBy(FIELD_SAVE_COUNT, Query.Direction.DESCENDING)
                    .limit(pageSize.toLong())
                    .let { base -> lastDocument?.let { base.startAfter(it) } ?: base }

            val snapshot = query.get().await()
            lastDocument = snapshot.documents.lastOrNull()
            Result.Success(snapshot.documents.map { it.id })
        } catch (e: Exception) {
            Result.Failure(e)
        }
}
