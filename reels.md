# Reels Feature

A full-screen, swipe-up game discovery experience. Each card fills the screen with the game's hero image, overlaid with rich info. Users swipe up to see the next game. They can heart a game to add it to favourites directly from the reel.

---

## Swipe direction

Standard reel UX: **swipe up** to advance to the next game (content moves upward, next card slides in from below). This is what users expect from TikTok/Reels/Shorts. `VerticalPager` handles this naturally.

---

## Screen layout (per page)

```
┌─────────────────────────────┐
│                        ♥    │  ← heart toggle (top end)
│                             │
│   [hero image, full bleed,  │
│    ContentScale.Crop]        │
│                             │
│ ▓▓▓▓ gradient scrim ▓▓▓▓▓▓ │  ← transparent → black, bottom 60%
│                             │
│  [85] ★ 4.2                 │  ← Metacritic badge + RAWG rating
│  Ghost of Tsushima          │  ← head4, bold, white
│  Action  RPG  Open World    │  ← genre chips
│                             │
│  An open-world action game  │  ← description, body3, 3 lines, white
│  set in feudal Japan...     │
│                             │
│  [screenshot] [screenshot]  │  ← horizontal carousel
│                             │
│  🕹 30h  PS4  PS5  PC       │  ← playtime + platform chips
└─────────────────────────────┘
```

---

## Domain models

### `feature/reels/domain/model/ReelGame.kt`
```kotlin
data class ReelGame(
    val id: String,
    val name: String,
    val description: String,
    val heroImage: String,
    val metacritic: Int?,
    val rating: Float,
    val genres: List<String>,
    val platforms: List<String>,
    val playtime: Int,
    val screenshots: List<String>,
)
```

---

## Repository interfaces

### `feature/reels/domain/repository/ReelGamesRepository.kt`
```kotlin
interface ReelGamesRepository {
    suspend fun getReelGameIds(): Result<List<String>>
}
```

### `feature/reels/domain/repository/GameDetailRepository.kt`
```kotlin
interface GameDetailRepository {
    suspend fun getGameDetail(id: String): Result<GameDetailApiModel>
    suspend fun getGameScreenshots(id: String): Result<List<String>>
}
```
Screenshots failure is non-fatal — return empty list rather than failing the whole card.

---

## Use cases

### `GetReelGameIdsUseCase`
Delegates to `ReelGamesRepository`. Single responsibility — returns the ordered list of IDs to show.

### `GetReelGameUseCase`
Takes an `id: String`. Calls both `getGameDetail` and `getGameScreenshots` in parallel via `coroutineScope { async { } }`. Combines into `ReelGame` via mapper. Screenshot failure = empty list, not an error.

---

## RAWG API

### New service: `GameDetailApiService`

```kotlin
interface GameDetailApiService {
    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") id: String): ApiResult<GameDetailApiModel>

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(@Path("id") id: String): ApiResult<ScreenshotsResponse>
}
```

### New API models

**`GameDetailApiModel`**
```kotlin
@Serializable
data class GameDetailApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description_raw") val descriptionRaw: String = "",
    @SerialName("background_image") val backgroundImage: String? = null,
    @SerialName("metacritic") val metacritic: Int? = null,
    @SerialName("rating") val rating: Float = 0f,
    @SerialName("playtime") val playtime: Int = 0,
    @SerialName("genres") val genres: List<GenreApiModel> = emptyList(),
    @SerialName("platforms") val platforms: List<PlatformWrapperApiModel> = emptyList(),
)
```

**`GenreApiModel`** — `{ "name": "Action" }`

**`PlatformWrapperApiModel`** — RAWG wraps platform: `{ "platform": { "name": "PC" } }`
```kotlin
@Serializable
data class PlatformWrapperApiModel(@SerialName("platform") val platform: PlatformApiModel)

@Serializable
data class PlatformApiModel(@SerialName("name") val name: String)
```

**`ScreenshotApiModel`** — `{ "id": 1, "image": "https://..." }`

**`ScreenshotsResponse`** — `{ "results": [...] }`

---

## Firebase data source (hardcoded for now)

### `ReelGamesFirestore`
```kotlin
internal class ReelGamesFirestore {
    suspend fun getReelGameIds(): Result<List<String>> =
        Result.Success(
            listOf(
                "4427",   // BioShock 2
                "638650", // Ghost of Tsushima
                "39",     // Prey (2017)
                "3328",   // The Witcher 3: Wild Hunt
            ),
        )
}
```
When Firebase is wired: reads ordered list from a Firestore collection (e.g. `reelGames`). The switch is a single-file change.

---

## Remove favourite (extends existing games feature)

The heart button needs to both add and remove. This requires extending the games feature:

### `FavouriteGamesRepository` — add method
```kotlin
suspend fun removeFavouriteGame(userId: String, gameId: String): Result<Unit>
```

### `RemoveFavouriteGameUseCase` (new, in `feature/games/domain/usecase/`)
```kotlin
class RemoveFavouriteGameUseCase(...) {
    suspend operator fun invoke(gameId: String): Result<Unit>
}
```

### `FavouriteGamesFirestore` — add method
```kotlin
suspend fun removeFavouriteGame(userId: String, gameId: String) {
    firestore
        .collection("users")
        .document(userId)
        .collection("favouriteGames")
        .document(gameId)
        .delete()
        .await()
}
```

### `FavouriteGamesRepositoryImpl` — implement the new method

---

## UI state

```kotlin
sealed interface ReelsUiState : BaseState {
    data object Loading : ReelsUiState
    data object Error : ReelsUiState
    data class Content(
        val pages: List<ReelPageState>,
        val favouriteIds: Set<String>,
    ) : ReelsUiState
}

sealed interface ReelPageState {
    data object Loading : ReelPageState
    data object Error : ReelPageState
    data class Loaded(val game: ReelGame) : ReelPageState
}
```

`favouriteIds` is a `Set<String>` of game IDs. The heart icon checks `game.id in favouriteIds` — no extra state needed per page.

---

## ViewModel

```kotlin
class ReelsViewModel(
    private val getReelGameIds: GetReelGameIdsUseCase,
    private val getReelGame: GetReelGameUseCase,
    private val getFavouriteGames: GetFavouriteGamesUseCase,
    private val addFavouriteGame: AddFavouriteGameUseCase,
    private val removeFavouriteGame: RemoveFavouriteGameUseCase,
) : BaseViewModel<ReelsUiState, ReelsAction>(ReelsUiState.Loading)
```

**Init:**
1. Launch parallel collection of favourites flow → keep `favouriteIds` up to date
2. Fetch game IDs → emit `Content(pages = ids.map { Loading }, favouriteIds = ...)`
3. Immediately trigger load of page 0, pre-fetch page 1

**`onPageVisible(index: Int)`**
Called when pager settles on a page. Triggers load of `pages[index]` if still `Loading`. Pre-fetches `pages[index + 1]`.

**`toggleFavourite(game: ReelGame)`**
Checks `game.id in favouriteIds`. Adds or removes accordingly.

---

## Screen structure

`ReelsScreen` uses `VerticalPager` (from `androidx.compose.foundation.pager`). Each page renders a `ReelPage` composable driven by its `ReelPageState`.

`ReelPage` when `Loaded`:
- `Box(fillMaxSize)` root
- `AsyncImage` full bleed background, `ContentScale.Crop`
- Gradient scrim: `Brush.verticalGradient` transparent → `Color.Black.copy(0.8f)` over bottom 60%
- Heart `IconButton` anchored top-end
- Content `Column` anchored bottom, with bottom padding for nav bar
- Screenshot `LazyRow` inside the column

`ReelPage` when `Loading`: full-screen shimmer (single `Box` with shimmer brush)

`ReelPage` when `Error`: centered retry option

---

## New files

```
feature/reels/
  domain/
    model/ReelGame.kt
    repository/ReelGamesRepository.kt
    repository/GameDetailRepository.kt
    usecase/GetReelGameIdsUseCase.kt
    usecase/GetReelGameUseCase.kt
    DomainModule.kt
  data/
    datasource/
      api/
        service/GameDetailApiService.kt
        model/GameDetailApiModel.kt
        model/GenreApiModel.kt
        model/PlatformWrapperApiModel.kt
        model/PlatformApiModel.kt
        model/ScreenshotApiModel.kt
        response/ScreenshotsResponse.kt
      firestore/ReelGamesFirestore.kt
    mapper/ReelGameMapper.kt
    repository/ReelGamesRepositoryImpl.kt
    repository/GameDetailRepositoryImpl.kt
    DataModule.kt
  presentation/
    screen/reels/
      ReelsUiState.kt
      ReelsAction.kt
      ReelsViewModel.kt
      ReelsScreen.kt
    PresentationModule.kt
  ReelsModule.kt
```

---

## Modified files

| File | Change |
|------|--------|
| `feature/games/domain/repository/FavouriteGamesRepository.kt` | Add `removeFavouriteGame` |
| `feature/games/domain/usecase/RemoveFavouriteGameUseCase.kt` | New |
| `feature/games/data/datasource/firestore/FavouriteGamesFirestore.kt` | Add `removeFavouriteGame` |
| `feature/games/data/repository/FavouriteGamesRepositoryImpl.kt` | Implement `removeFavouriteGame` |
| `feature/games/domain/DomainModule.kt` | Register `RemoveFavouriteGameUseCase` |
| `base/presentation/navigation/NavigationScreens.kt` | Add `Reels` |
| `base/presentation/MainScreen.kt` | Add `Reels` composable |
| `base/presentation/BottomNavigationBar.kt` | Add Reels tab (`ic_play_arrow`) |
| `base/Application.kt` | Add `featureReelsModules` |
| `strings.xml` | Add reel strings |

---

## What is NOT in scope

- Firebase wiring for the game IDs list (hardcoded, clearly marked for replacement)
- Deep-linking into a specific game's reel
- Share / report game actions
- Infinite scroll / pagination beyond the hardcoded list
- Expanding the description to full-screen

---

## Order of implementation

1. Extend games feature: `removeFavouriteGame` through all layers
2. Domain layer: `ReelGame`, repository interfaces, use cases
3. Data layer: API models + service, `ReelGamesFirestore`, mapper, repository impls
4. Koin modules: `DataModule`, `DomainModule`, `PresentationModule`, `ReelsModule`
5. UI: `ReelsUiState`, `ReelsAction`, `ReelsViewModel`, `ReelsScreen`
6. Navigation wiring: `NavigationScreens`, `MainScreen`, `BottomNavigationBar`
7. `Application.kt` + `strings.xml`
8. Build, spotless, detekt
