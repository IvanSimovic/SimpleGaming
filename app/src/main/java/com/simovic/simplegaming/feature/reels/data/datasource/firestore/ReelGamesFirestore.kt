@file:Suppress("TooGenericExceptionCaught")

package com.simovic.simplegaming.feature.reels.data.datasource.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.simovic.simplegaming.base.domain.result.Result
import kotlinx.coroutines.tasks.await

private const val COLLECTION_GAME_STATS = "gameStats"
private const val FIELD_SAVE_COUNT = "saveCount"

internal class ReelGamesFirestore(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getReelGameIds(): Result<List<String>> =
        try {
            val snapshot =
                firestore
                    .collection(COLLECTION_GAME_STATS)
                    .orderBy(FIELD_SAVE_COUNT, Query.Direction.DESCENDING)
                    .get()
                    .await()
            Result.Success(snapshot.documents.map { it.id })
        } catch (e: Exception) {
            Result.Failure(e)
        }
}
