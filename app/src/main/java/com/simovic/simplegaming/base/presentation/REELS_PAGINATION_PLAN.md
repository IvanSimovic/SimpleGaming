# Reels Pagination — Plan

## What and Why

Right now the Reels screen fetches every game ID from `gameStats` upfront.
With 100,000 games that's 100,000 document reads on every open — slow and expensive.

Pagination loads IDs in batches of 20. The user sees the first 20 games immediately.
As they swipe and approach the end of the loaded batch, the next 20 are fetched silently.
From the user's perspective the feed is infinite. Under the hood it's controlled and cheap.

---

## Cursor Strategy

Firestore paginates via `startAfter(DocumentSnapshot)`. The last document from each
page becomes the cursor for the next. This cursor must not leak into the domain layer —
it's a Firestore implementation detail.

`ReelGamesFirestore` becomes stateful: it holds the last `DocumentSnapshot?` internally.
Each call to `getNextPage()` uses the stored cursor and updates it.
A `reset()` clears the cursor for when the ViewModel is recreated.

Since `ReelGamesFirestore` is a Koin `single`, it lives as long as the app. The ViewModel
calls `reset()` on init to ensure a clean slate.

---

## Favourites Filtering

Favourites are fetched once on Reels open (already done via `getFavouriteGameIds`).
The set is held in the ViewModel. Each incoming page of IDs is filtered against it
client-side before being added to the pager. If filtering leaves too few IDs, the next
page is fetched immediately and transparently.

---

## Architecture Changes

**`ReelGamesFirestore`**
- Holds `lastDocument: DocumentSnapshot?`
- `suspend fun getNextPage(pageSize: Int): Result<List<String>>`
- `fun reset()`

**`ReelGamesRepository` interface**
- Replace `getReelGameIds()` with `suspend fun getNextReelGameIds(pageSize: Int): Result<List<String>>`
- Add `fun resetPaging()`

**`ReelGamesRepositoryImpl`**
- Delegates both methods to `ReelGamesFirestore`

**`GetReelGameIdsUseCase`**
- Gains `resetPaging()` pass-through
- `invoke(pageSize)` fetches next page, filters saved IDs, returns clean list
- Saved IDs set is passed in from the ViewModel (already fetched once on init)

**`ReelsAction`**
- Add `AppendPages(ids: List<String>)` — adds new `ReelPageState.Loading` entries to the pager

**`ReelsViewModel`**
- On init: fetch saved IDs → reset paging → fetch first page → build initial pager
- Holds `savedIds: Set<String>` and `isLoadingMoreIds: Boolean`
- In `onPageVisible(index)`: when `index >= pages.size - 5` and not already loading,
  fetch next page and append

---

## Page Size

20 IDs per fetch. Gives the user a comfortable buffer without over-fetching.

---

## End of Feed

When `getNextPage()` returns an empty list, no more fetches are triggered.
The pager simply ends at whatever was last loaded.
