# Feature: Auth — Login

Firebase Email/Password authentication. Login screen only.
No register, no password reset, no forgot password.

This feature also owns the `userId` that the games feature needs.
The `UserConfig` placeholder in `games/feature.md` is replaced by `GetCurrentUserIdUseCase` from this feature.

---

## The Navigation Problem

The app currently boots straight into `MainShowcaseScreen` with no gate.
Auth changes the root: the entire app now has two worlds — unauthenticated and authenticated —
and something at the top level must decide which world to show.

That something is `AppViewModel`, a new ViewModel in `base/presentation`.
It observes `ObserveAuthStateUseCase` as a Flow and exposes an `AppUiState`.
`MainActivity` renders `AppScreen` (also new, in `base/presentation`) which switches
composition based on that state.

```
MainActivity
  └── AppScreen                  ← new, replaces MainShowcaseScreen as the root
        ├── Checking              ← splash screen held until resolved
        ├── Unauthenticated       ← shows LoginScreen
        └── Authenticated         ← shows MainShowcaseScreen (existing)
```

The splash screen is already installed via `installSplashScreen()` in `MainActivity`.
`setKeepOnScreenCondition` will hold it while `AppUiState` is `Checking`, preventing
a flash of the login screen on cold start when the user is already logged in.

---

## Auth State Flow

Firebase provides `FirebaseAuth.authStateChanges()` — a `Flow<FirebaseUser?>`.
- `null` → logged out
- non-null → logged in, userId available via `firebaseUser.uid`

This is the single source of truth. The `LoginViewModel` does not navigate on success.
It fires the sign-in call and waits. Firebase auth state emits the new user.
`AppViewModel` picks that up and switches `AppUiState` to `Authenticated`.
Composition switches. The login screen disappears. No explicit navigation needed.

---

## Domain Models

```kotlin
// Pure Kotlin — no Firebase imports
sealed interface AuthState {
    data class LoggedIn(val userId: String) : AuthState
    data object LoggedOut : AuthState
}
```

---

## Repository Contract

```kotlin
interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>
    suspend fun signIn(email: String, password: String): Result<Unit>
    fun getCurrentUserId(): String?
}
```

`getCurrentUserId()` is synchronous — Firebase Auth caches the current user.
Used by the games feature use cases to get the userId without subscribing to a Flow.

---

## Use Cases

```kotlin
class ObserveAuthStateUseCase(private val repo: AuthRepository) {
    operator fun invoke(): Flow<AuthState> = repo.observeAuthState()
}

class SignInUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repo.signIn(email, password)
}

class GetCurrentUserIdUseCase(private val repo: AuthRepository) {
    operator fun invoke(): String? = repo.getCurrentUserId()
}
```

---

## AppUiState (base/presentation)

```kotlin
sealed interface AppUiState {
    data object Checking : AppUiState        // initial — observing Firebase, not yet known
    data object Unauthenticated : AppUiState
    data class Authenticated(val userId: String) : AppUiState
}
```

`AppViewModel` collects `ObserveAuthStateUseCase` in `init {}`:
- First emission maps to `Unauthenticated` or `Authenticated`
- Before first emission → `Checking` (splash held)

---

## LoginUiState

```kotlin
sealed interface LoginUiState : BaseState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
}
```

No `Success` state. Success is expressed through the auth state Flow —
when Firebase confirms sign-in, `AppViewModel` switches worlds automatically.

---

## LoginViewModel Behaviour

- `signIn(email, password)` called from screen
- Validates: empty email or empty password → `Error` with local message, no network call
- Otherwise → `Loading`, calls `SignInUseCase`
- `Result.Success` → do nothing, Firebase auth state Flow handles navigation
- `Result.Failure` → `Error(throwable.message ?: "Sign in failed")`

---

## LoginScreen

- Email field (`AppTextField`, keyboard type email, ime action Next)
- Password field (`AppTextField`, obscured, ime action Done → triggers sign in)
- Sign In button (`AppButton`, disabled while `Loading`, shows loading spinner)
- Error message below the button (visible only in `Error` state)
- No back navigation (login is the root when unauthenticated)

---

## Impact on games/feature.md

Remove the `UserConfig` section entirely.

`GetFavouriteGamesUseCase` and `AddFavouriteGameUseCase` inject `GetCurrentUserIdUseCase`
from this feature instead of `UserConfig`. If `getCurrentUserId()` returns null when
these use cases are called, return `Result.Failure` — this should not happen in practice
since the games feature is only reachable when `AppUiState` is `Authenticated`.

---

## Changes to Existing Files

| File | Change |
|------|--------|
| `base/presentation/MainActivity.kt` | Observe `AppViewModel`, hold splash on `Checking` |
| `base/presentation/MainScreen.kt` | No structural change — still renders `MainShowcaseScreen` |
| `base/AppKoinModule.kt` | Register `AppViewModel` |
| `base/Application.kt` | Register `featureAuthModules` |
| New: `base/presentation/AppScreen.kt` | Root composable switching on `AppUiState` |
| New: `base/presentation/AppViewModel.kt` | Observes auth state, exposes `AppUiState` |

---

## Folder Structure

```
feature/auth/
  AuthModule.kt                          ← assembles data + domain + presentation
  data/
    DataModule.kt
    datasource/
      firebase/
        FirebaseAuthDataSource.kt        ← wraps FirebaseAuth, maps to domain
    repository/
      AuthRepositoryImpl.kt
  domain/
    DomainModule.kt
    model/
      AuthState.kt
    repository/
      AuthRepository.kt
    usecase/
      ObserveAuthStateUseCase.kt
      SignInUseCase.kt
      GetCurrentUserIdUseCase.kt
  presentation/
    PresentationModule.kt
    screen/
      login/
        LoginScreen.kt
        LoginViewModel.kt
        LoginUiState.kt
        LoginAction.kt
```

---

## Dependencies to Add

```kotlin
// Firebase Auth (BOM already planned for games feature — shared)
implementation("com.google.firebase:firebase-auth-ktx")

// google-services plugin + google-services.json already required for Firestore
```

---

## Edge Cases

- **Already logged in on cold start** — `authStateChanges()` emits immediately with the
  cached user. Splash stays visible for the duration of `Checking`. No login screen flash.
- **Invalid credentials** — Firebase returns `FirebaseAuthInvalidCredentialsException`.
  Surface `exception.message` in `Error` state.
- **Network offline during sign in** — Firebase throws, caught as `Result.Failure`,
  shown as `Error`. No retry logic required for v1.
- **Empty fields** — validated locally before any network call. Specific messages:
  "Email is required" / "Password is required".
- **Back press on login screen** — finishes the app (login is the root destination,
  no back stack above it).
- **Token expiry while app is open** — `authStateChanges()` emits `null`,
  `AppViewModel` switches to `Unauthenticated`, login screen appears automatically.
