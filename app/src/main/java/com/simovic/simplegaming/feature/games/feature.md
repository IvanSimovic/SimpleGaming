# Feature: Favourite Games

Display and manage a user's favourite games. Games are stored in Firestore under a user ID.
New games are discovered via an external search API and saved to Firestore.

---

## Screens

### 1. FavouriteGamesScreen (list)
- 2-column image grid of the user's saved games
- Each cell: game cover image, game name below
- FAB (via ScaffoldController) navigates to AddGameScreen
- States: Loading, Content (grid), Empty (no favourites yet), Error

### 2. AddGameScreen (search + add)
- Search bar at the top, debounced (500ms)
- Results shown as a 2-column image grid (same layout as list screen)
- Tap a result to add it to favourites, then navigate back
- States: Idle (no query yet), Loading, Content (results), Empty (no results), Error

---

## Data Sources

### Firestore
Path: `users/{userId}/favouriteGames/{gameId}`

Document fields:
```
gameId    : String    — stable ID from the search API
name      : String    — display name
imageUrl  : String    — cover image URL
addedAt   : Timestamp — when the user added it
```

Reading uses a **real-time snapshot listener** (returns a Flow). This means the
FavouriteGamesScreen updates automatically when a game is added — no manual refresh needed.
Writing is a one-shot suspend call.

### Game Search API
External REST API. Base URL comes from `BuildConfig.apiBaseUrl` (placeholder for now).

```
GET /games/search?q={query}&limit=10
Authorization: Bearer {apiToken}      ← BuildConfig.apiToken

Response 200:
{
  "results": [
    {
      "id": "string",
      "name": "string",
      "imageUrl": "string"
    }
  ]
}

Response 4xx/5xx: handled as Result.Failure
```

---

## Domain Models

```kotlin
// Pure Kotlin — no Android, no Firebase, no Room
data class Game(
    val id: String,
    val name: String,
    val imageUrl: String,
)

data class FavouriteGame(
    val gameId: String,
    val name: String,
    val imageUrl: String,
    val addedAt: Long,   // epoch millis
)
```

---

## Repository Contracts

```kotlin
interface FavouriteGamesRepository {
    fun getFavouriteGames(userId: String): Flow<Result<List<FavouriteGame>>>
    suspend fun addFavouriteGame(userId: String, game: Game): Result<Unit>
}

interface GameSearchRepository {
    suspend fun searchGames(query: String): Result<List<Game>>
}
```

Note: `getFavouriteGames` returns a `Flow`, not a suspend function. Firestore's snapshot
listener is inherently streaming. The ViewModel collects it in `init {}` — when a game is
added via Firestore, the Flow emits automatically and the list updates with no extra work.

---

## Use Cases

```kotlin
// Returns a Flow — ViewModel collects it
class GetFavouriteGamesUseCase(
    private val repo: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    operator fun invoke(): Flow<Result<List<FavouriteGame>>> =
        repo.getFavouriteGames(userConfig.userId)
}

// One-shot write
class AddFavouriteGameUseCase(
    private val repo: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    suspend operator fun invoke(game: Game): Result<Unit> =
        repo.addFavouriteGame(userConfig.userId, game)
}

// One-shot search
class SearchGamesUseCase(
    private val repo: GameSearchRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Game>> =
        repo.searchGames(query)
}
```

---

## User ID

Provided by `GetCurrentUserIdUseCase` from the `feature/auth` domain layer.
Injected directly into `GetFavouriteGamesUseCase` and `AddFavouriteGameUseCase`.

If `getCurrentUserId()` returns null, the use case returns `Result.Failure` immediately.
This should not happen in practice — the games feature is only reachable when
`AppUiState` is `Authenticated`, which guarantees a non-null userId.

---

## UI State

### FavouriteGamesUiState
```kotlin
sealed interface FavouriteGamesUiState : BaseState {
    data object Loading : FavouriteGamesUiState
    data object Empty : FavouriteGamesUiState
    data object Error : FavouriteGamesUiState
    data class Content(val games: List<FavouriteGameUiModel>) : FavouriteGamesUiState
}

data class FavouriteGameUiModel(
    val gameId: String,
    val name: String,
    val imageUrl: String,
)
```

### AddGameUiState
```kotlin
sealed interface AddGameUiState : BaseState {
    data object Idle : AddGameUiState        // no query entered yet
    data object Loading : AddGameUiState
    data object Empty : AddGameUiState       // query returned no results
    data object Error : AddGameUiState
    data object Added : AddGameUiState       // game was added, trigger navigation back
    data class Content(val results: List<Game>) : AddGameUiState
}
```

---

## ViewModel Behaviour

### FavouriteGamesViewModel
- Collects `GetFavouriteGamesUseCase` Flow in `init {}`
- Empty list → `Empty` state, not `Error`
- Flow error → `Error` state
- No manual refresh needed — Firestore snapshot handles it

### AddGameViewModel
- Query held in a `MutableStateFlow<String>`
- `debounce(500)` + `distinctUntilChanged()` + `filter { length >= 2 || isBlank() }`
- Blank query → `Idle` state
- Non-blank query → `Loading` then `Content` / `Empty` / `Error`
- `addGame(game)` calls `AddFavouriteGameUseCase`, on success emits `Added`
- Screen observes `Added` state and calls `onNavigateBack()`

---

## Navigation

New routes in `NavigationScreens`:
```kotlin
@Serializable object FavouriteGames : NavigationScreens
@Serializable object AddGame : NavigationScreens
```

Changes to `MainScreen`:
- `startDestination` → `NavigationScreens.FavouriteGames`
- `FavouriteGamesScreen` registered with FAB setup (ScaffoldController) same pattern as BirthDayList
- `AddGameScreen` registered, navigated to from FAB, back arrow returns to list

Changes to `BottomNavigationBar`:
- Single item: Games (for now — other features removed from nav, not from codebase)

---

## Folder Structure

```
feature/games/
  GamesModule.kt                          ← assembles data + domain + presentation
  data/
    DataModule.kt
    datasource/
      api/
        service/GamesApiService.kt
        model/GameApiModel.kt
        response/SearchGamesResponse.kt
      firestore/
        model/FavouriteGameFirestoreModel.kt
    mapper/
      GameMapper.kt
      FavouriteGameMapper.kt
    repository/
      GameSearchRepositoryImpl.kt
      FavouriteGamesRepositoryImpl.kt
  domain/
    DomainModule.kt
    model/
      Game.kt
      FavouriteGame.kt
      UserConfig.kt
    repository/
      FavouriteGamesRepository.kt
      GameSearchRepository.kt
    usecase/
      GetFavouriteGamesUseCase.kt
      AddFavouriteGameUseCase.kt
      SearchGamesUseCase.kt
  presentation/
    PresentationModule.kt
    screen/
      favouritegames/
        FavouriteGamesScreen.kt
        FavouriteGamesViewModel.kt
        FavouriteGamesUiState.kt
        FavouriteGamesAction.kt
        FavouriteGameUiModel.kt
      addgame/
        AddGameScreen.kt
        AddGameViewModel.kt
        AddGameUiState.kt
        AddGameAction.kt
```

---

## Dependencies to Add

```kotlin
// Firebase BOM + Firestore
implementation(platform("com.google.firebase:firebase-bom:{version}"))
implementation("com.google.firebase:firebase-firestore-ktx")

// google-services plugin required in app/build.gradle.kts
// google-services.json required in app/ (placeholder for now)
```

---

## Edge Cases

- **Empty favourites** — show a dedicated Empty state with a prompt to add the first game
- **Duplicate add** — if a game with the same `gameId` already exists in Firestore,
  `set()` with merge will overwrite silently (acceptable behaviour for now)
- **Add while offline** — Firestore SDK queues the write and syncs when back online;
  the UI should optimistically show `Added` and navigate back regardless
- **Search returns empty** — `Empty` state, not `Error`
- **Network failure on search** — `Error` state with retry not required for v1
- **Back pressed during add** — navigates back with no side effects; Firestore write
  is either already committed or will commit when back online
- **Image load failure** — placeholder image (existing `PlaceholderImage` component)
