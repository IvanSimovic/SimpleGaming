# Testing Plan

## Why
The architecture is solid but untested. Tests are what turn good architecture into a provable guarantee — and what make this codebase something you can defend in an interview, not just show.

## Setup ✅
Turbine added to `libs.versions.toml` and the test bundle.
`FavouriteGamesViewModel` uses `UnconfinedTestDispatcher` — coroutines run immediately, state transitions are observable via Turbine.
`ReelsViewModel` uses `StandardTestDispatcher` — coroutines are controlled manually via `advanceUntilIdle()`, enabling precise state capture.
Fakes used at every layer boundary. MockK used only for concrete Firestore datasource classes that cannot be subclassed.

## Order
1. ✅ Use cases — isolated, pure Kotlin, fastest to write and highest confidence per line
2. ✅ `FavouriteGamesViewModel` — simpler ViewModel, establishes the MVI test pattern
3. ✅ `ReelsViewModel` — the most complex logic in the project; pagination and prefetch coverage is the headline test
4. ✅ Repositories — thin layer, but exception-wrapping and model mapping need a guarantee

## Non-obvious decisions
- `GetReelGameIdsUseCase`: all ids filtered out by savedIds is `Success(emptyList())`, not `Failure`
- `GetFavouriteGamesUseCase`: blank userId returns `Failure` immediately without touching the repo
- Repository tests fake the Firestore datasource — they test wrapping and mapping, not Firestore itself
- `ReelsViewModel` pagination: `loadNextPage` called while already loading is a no-op, not a race

## Fake locations
`test/.../fake/` — one fake per repository interface, reused across use case and ViewModel tests.
