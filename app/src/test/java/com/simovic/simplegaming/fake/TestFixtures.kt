package com.simovic.simplegaming.fake

import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame

const val VALID_USER_ID = "user-1"
const val GAME_ID_1 = "id-1"
const val GAME_ID_2 = "id-2"
const val GAME_ID_3 = "id-3"
const val GAME_NAME = "Game 1"
const val PAGE_SIZE = 20
const val PREFETCH_THRESHOLD = 5

fun aGame(id: String = GAME_ID_1) = Game(id = id, name = GAME_NAME, imageUrl = "")

fun aReelGame(id: String = GAME_ID_1) = ReelGame(
    id = id,
    name = GAME_NAME,
    description = "",
    heroImage = "",
    metacritic = null,
    rating = 0f,
    genres = emptyList(),
    platforms = emptyList(),
    playtime = 0,
    screenshots = emptyList(),
)
