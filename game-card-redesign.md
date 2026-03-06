# Game Card Redesign

## Goal

Replace the current image + title column layout with an image-first card. Title is hidden when an image is present. When the image URL is blank, title is shown centered inside the card as a fallback. Images are cropped to fill the card cleanly.

---

## Visual design

**With image:**
```
┌──────────────┐
│              │
│  [cropped    │
│   image]     │
│              │
└──────────────┘
```

**Without image (fallback):**
```
┌──────────────┐
│              │
│  Game Title  │  ← centered, body2, textMuted
│              │
└──────────────┘
```

- Portrait aspect ratio (3:4, `aspectRatio(0.75f)`) — fills a good chunk of vertical space, two cards fit comfortably side by side
- Rounded corners: `Dimen.spaceM` (8dp)
- Card elevation: minimal (`CardDefaults.cardElevation(defaultElevation = 2.dp)`)
- Image: `ContentScale.Crop` — fills the card, cropped to square
- Fallback background: `AppTheme.color.surfaceHigh`
- Fallback title: `AppTheme.typo.body2`, `AppTheme.color.textMuted`, centered, max 2 lines

---

## Files changed

### 1. `base/presentation/compose/composable/PlaceholderImage.kt`

Add `contentScale: ContentScale = ContentScale.Fit` parameter.
Add `Modifier.fillMaxSize()` to the `AsyncImage` so it fills whatever bounds the modifier provides.

```kotlin
@Composable
fun PlaceholderImage(
    url: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Surface(modifier = modifier) {
        AsyncImage(
            model = ...,
            contentDescription = contentDescription,
            placeholder = painterResource(randomPlaceHolder),
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
```

No other callers are broken — default remains `ContentScale.Fit`.

---

### 2. `feature/games/presentation/screen/favouritegames/FavouriteGamesScreen.kt`

Replace `GameCell`:

```kotlin
@Composable
private fun GameCell(
    game: FavouriteGameUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(Dimen.spaceM),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.aspectRatio(GAME_CARD_ASPECT_RATIO),
    ) {
        if (game.imageUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize().background(AppTheme.color.surfaceHigh),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = game.name,
                    style = AppTheme.typo.body2,
                    color = AppTheme.color.textMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(Dimen.spaceM),
                )
            }
        } else {
            PlaceholderImage(
                url = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
```

---

### 3. `feature/games/presentation/screen/addgame/AddGameScreen.kt`

Replace `SearchResultCell` with the same card approach. The cell is also clickable — `Card` accepts `onClick` directly.

```kotlin
@Composable
private fun SearchResultCell(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(Dimen.spaceM),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.aspectRatio(GAME_CARD_ASPECT_RATIO),
    ) {
        if (game.imageUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize().background(AppTheme.color.surfaceHigh),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = game.name,
                    style = AppTheme.typo.body2,
                    color = AppTheme.color.textMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(Dimen.spaceM),
                )
            }
        } else {
            PlaceholderImage(
                url = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
```

Note: `Column` wrapper and the always-visible `Text` below are removed. The `clickable` modifier is replaced by `Card(onClick = ...)`.

---

### 4. `feature/games/presentation/GameGridShimmer.kt`

Update shimmer items to match the card layout — single card-shaped shimmer box, no separate text shimmer row.

```kotlin
items(SHIMMER_ITEM_COUNT) {
    Card(
        shape = RoundedCornerShape(Dimen.spaceM),
        modifier = Modifier.aspectRatio(1f),
    ) {
        Box(modifier = Modifier.fillMaxSize().background(brush))
    }
}
```

Remove `SHIMMER_TEXT_WIDTH_FRACTION` and `Dimen.textHeight` usage — no longer needed.

---

### 5. `Dimen.kt`

Add card elevation constant (2dp):

```kotlin
val cardElevation = 2.dp
```

This avoids a magic number in the card composables.

---

### 6. `GameGridShimmer.kt` / `FavouriteGamesScreen.kt` / `AddGameScreen.kt`

Add a private file-level constant for the aspect ratio to avoid a magic number:

```kotlin
private const val GAME_CARD_ASPECT_RATIO = 0.75f  // 3:4 portrait
```

Note: RAWG images are landscape (16:9). `ContentScale.Crop` on a portrait card will show the horizontal center of the image — typically where the main subject sits. The crop is intentional and looks natural.

---

## What does NOT change

- `FavouriteGameUiModel` — no new fields needed, `imageUrl` blank check is sufficient
- `Game` domain model — same
- Grid layout (`LazyVerticalGrid`, 2 columns, spacing) — unchanged
- Navigation — unchanged
- Any other screen

---

## Order of implementation

1. `Dimen.kt` — add `cardElevation`
2. `PlaceholderImage.kt` — add `contentScale` param
3. `GameGridShimmer.kt` — update shimmer items to card layout, remove unused constants
4. `FavouriteGamesScreen.kt` — update `GameCell`
5. `AddGameScreen.kt` — update `SearchResultCell`
6. Run spotless + detekt
