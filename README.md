# SimpleGaming

An Android application built to demonstrate Clean Architecture and MVI on a single-module setup.

---

## Architecture

The project follows Clean Architecture with strict layer separation across every feature.

```
presentation  →  domain  ←  data
```

- **Presentation** — ViewModels, UI state, Compose screens.
- **Domain** — Use cases, repository interfaces, domain models.
- **Data** — Repository implementations, network and database sources, mappers. Hidden behind the repository interface.

### MVI

State management follows a unidirectional MVI pattern built on a shared `BaseViewModel`:

```
User event → Action → reduce(currentState) → new State → UI
```

Each `Action` is a pure function that takes the current state and returns the next one. Duplicate states are deduplicated before emission — the UI only receives a new state when something actually changed. A `StateTimeTravelDebugger` logs every action and state transition in debug builds.

### Result type

Errors propagate through layers via a sealed `Result<T>`:

```kotlin
sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(val throwable: Throwable? = null) : Result<Nothing>
}
```

The data layer produces it. The domain layer transforms it. The presentation layer consumes it. Nothing leaks across boundaries.

---

## Features

### Authentication
Firebase Authentication handles sign-in. The auth state gates access to the rest of the app.

### Games
Searches games via the [RAWG API](https://rawg.io/apidocs). Results are displayed with cover art and metadata. Games can be marked as favourites, which are persisted in Firestore and tied to the authenticated user.

### Reels
Displays a curated feed of gaming video content sourced from Firestore. Each reel is rendered inline in a vertically scrolling list.

---

## Tech Stack

| Concern | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation Compose (type-safe, serialised routes) |
| State | Kotlin Coroutines, StateFlow |
| Dependency injection | Koin |
| Network | Retrofit, OkHttp, Kotlinx Serialization (JSON) |
| Firebase | Firebase Auth, Firebase Firestore |
| Image loading | Coil |
| Animations | Lottie |
| Logging | Timber |
| Static analysis | Detekt, Spotless / ktlint, Konsist |
| Testing | JUnit 5, MockK, Kluent, Turbine |

---

## Static Analysis & Architecture Tests

The project uses [Konsist](https://github.com/LemonAppDev/konsist) to enforce architecture rules as automated tests — verifying layer boundaries, naming conventions, and dependency direction at build time rather than in code review.

Detekt and Spotless (ktlint) run on every build to enforce code style and catch common issues.

---

## Claude Code Setup

The project is configured for expansion with [Claude Code](https://claude.ai/code). A set of domain-specific skills covering areas like architecture, state, navigation, and testing are defined in `.claude/skills/`.

The skills are written as engineering principles — the kind that come from experience and carry a point of view — rather than lists of instructions. This means Claude reasons from understanding rather than executing steps, and produces code that fits the existing architecture instead of code that merely compiles.
