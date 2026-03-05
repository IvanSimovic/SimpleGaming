# TempScreen Plan

Throwaway test screen. Goal: verify auth round-trip works end-to-end (login → main app → sign out → login screen reappears). Will be deleted once the games feature has a real screen to test against.

---

## Sign Out Flow

Sign out is a one-line Firebase call (`FirebaseAuth.signOut()`), but it belongs in the auth feature's domain layer — not in the screen.

The flow after sign out:
- `SignOutUseCase` calls `repo.signOut()`
- Firebase `authStateChanges()` emits `null`
- `AppViewModel` receives `LoggedOut` → sends `SetUnauthenticated` action
- `AppScreen` switches composition to `LoginScreen`

No explicit navigation needed. Same mechanism as login — the auth state Flow handles everything.

---

## What Changes

### Auth feature — new additions only, nothing removed

| File | Change |
|------|--------|
| `FirebaseAuthDataSource` | Add `fun signOut()` → calls `firebaseAuth.signOut()` |
| `AuthRepository` | Add `fun signOut()` |
| `AuthRepositoryImpl` | Implement `signOut()` |
| `SignOutUseCase` (new) | Wraps `repo.signOut()`, `operator fun invoke()` |
| `DomainModule` | Register `SignOutUseCase` via `singleOf` |

### New files in base/presentation

| File | Purpose |
|------|---------|
| `TempViewModel.kt` | Injects `SignOutUseCase`, exposes `signOut()` function |
| `TempScreen.kt` | Single button: "Sign Out", calls `viewModel.signOut()` |

### Existing files to update

| File | Change |
|------|--------|
| `NavigationScreens.kt` | Add `@Serializable object Temp` |
| `MainScreen.kt` | `startDestination` → `Temp`, add `composable<Temp>` route |
| `BottomNavigationBar.kt` | Replace all items with single `Temp` item |
| `AppKoinModule.kt` | Register `TempViewModel` via `viewModelOf` |

---

## TempViewModel

Simple — no state, no actions, no BaseViewModel needed. It just calls a use case.
A plain `ViewModel` is correct here: there is no UI state to manage.

```kotlin
class TempViewModel(private val signOutUseCase: SignOutUseCase) : ViewModel() {
    fun signOut() = signOutUseCase()
}
```

---

## TempScreen

Centered `AppButton` labelled "Sign Out". No top bar, no state to observe.
Lives at `base/presentation/TempScreen.kt` — not in a feature folder, because it is not a feature.

---

## What Does NOT Change

- `AppViewModel` — already handles `LoggedOut` correctly
- `AppScreen` — already switches to `LoginScreen` on `Unauthenticated`
- `LoginScreen` — untouched
- All existing features — untouched, just unreachable from bottom nav for now
