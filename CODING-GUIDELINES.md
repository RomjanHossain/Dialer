# Coding Guidelines & Best Practices

## 1. Kotlin & Compose Style
- **Language:** Kotlin 1.9+, strict adherence to [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Formatting:** `ktlint` (with `compose` ruleset) and `detekt` for static analysis. CI enforces zero warnings.
- **Compose‑specific:**
  - No `var` for state in composables; use `remember`, `mutableStateOf`, or hold state in ViewModel.
  - Side‑effect APIs (`LaunchedEffect`, `DisposableEffect`, `SideEffect`) only for framework interactions.
  - Composables are pure functions of state – no business logic.

## 2. Project Organisation
- **Package by feature** inside each module, e.g., `com.dialer.feature.incall.ui`, `.domain`, `.data`.
- **One public composable per screen**, typically named `[ScreenName]Screen(state, events)`.
- Subcomponents are private and located in the same file or a `components` package.

## 3. Naming Conventions

### Compose Components
- Use `PascalCase` for composable functions: `DialpadButton`, `ContactListItem`.
- The main screen composable ends with `Screen` (e.g., `RecentsScreen`).
- If a composable takes no parameters and has no state, prefix with `Dialer` to avoid ambiguity: `DialerTheme`, `DialerDivider`.

### ViewModels & Use Cases
- ViewModel class: `[Feature]ViewModel` (e.g., `DialpadViewModel`).
- Use Case class: verb‑noun style, `[Action][Entity]UseCase` (e.g., `PlaceCallUseCase`, `ObserveActiveCallUseCase`).
- Repository interface: `[Entity]Repository`. Implementation: `[Entity]RepositoryImpl`.

### State & Events
- State class: `[Feature]UiState` (e.g., `DialpadUiState`). Immutable data class.
- Event class: `[Feature]UiEvent` (e.g., `DialpadUiEvent`). Sealed interface for one‑shots (e.g., `NavigateToCall`).

## 4. Architecture Rules
- **Domain layer pure:** No `android.*` imports, no `Context`. Use `kotlinx.coroutines.flow.Flow` for streams.
- **ViewModels only depend on Use Cases**, never on repositories directly.
- **Data sources** are injected into repositories, never used by ViewModels/Use Cases.
- **No passing `Context` into the Domain/Data layer** except via dependency injection (`ApplicationContext` in a module setup).
- **Composables never call ViewModel directly** from deep child components – pass events through lambdas up to the screen root.

## 5. State Management Pattern (UDF)
Every screen follows this exact pattern:

```kotlin
@Composable
fun RecentsScreen(viewModel: RecentsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    // One-shot events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecentsUiEvent.ShowSnackbar -> // show snackbar
            }
        }
    }
    RecentsContent(state = state, onEvent = viewModel::onEvent)
}

```
ViewModel holds _state: MutableStateFlow<UiState>, and _events: Channel<UiEvent>.

All user interactions are packaged into a sealed Event class, sent to ViewModel via a single onEvent(event) function.
6. Theming & Design System

    No Material Theme anywhere. Use a custom DialerTheme composable that provides:
    kotlin

    DialerTheme(darkTheme = isDark) {
        // Content
    }

    Inside, DialerColorScheme, DialerTypography, and DialerShape are held in CompositionLocal.

    All visual constants (dimensions, opacities, elevation) defined in a DesignTokens object inside core:ui.

    Composables that need design tokens use these locals, never hardcode colors/dimens.

7. Animation Guidelines

    Use androidx.compose.animation.core.* directly.

    Keep animations in separate Modifier extensions or AnimationSpec objects.

    Avoid animating layout (width/height) for performance; prefer scale and alpha transformations.

    All long‑running animations (pulses, recording indicator) use rememberInfiniteTransition and are optimised with Animatable.

8. Performance & Best Practices

    Stable lists: Use LazyColumn with key and contentType when items are heterogeneous.

    Modifier order: Order modifiers carefully; clickable before padding, etc.

    State hoisting: Lift state to the lowest common ancestor that needs it.

    Avoid recomposition: Use derivedStateOf for computed values, and @Stable/@Immutable annotations on model classes.

    Profiling: R8 full mode, baseline profiles for launch, startup tracing for key screens.

9. Call Recording & Security

    All recording-related code must log only minimal metadata (no raw audio in logs).

    Use SecurityException handling for permission denials gracefully.

    Encryption keys must be generated and stored using Android Keystore (API 23+). Never hardcode keys.

    Audio files are stored in app‑specific internal storage, not shared storage, unless user explicitly exports.

    Background service notification is mandatory and must show “Recording in progress” with a stop action.

10. Testing Requirements

    Every use case must have unit tests covering success, error, and edge cases.

    ViewModels tested with kotlinx-coroutines-test and Turbine for flows.

    Composable previews for light/dark states and all screen sizes.

    Screenshot tests (using robolectric + ComposeTestRule) ensure design regressions are caught.

    Instrumented tests for recording pipeline on real devices.

11. Git & Collaboration

    Branch naming: feature/JIRA-123-short-description, bugfix/....

    Commit messages: conventional commits (feat:, fix:, refactor:).

    Pull requests require two approvals and CI pass (lint, unit test, detekt).

    No commented‑out code in PRs; delete unused imports.

12. Documentation

    Public API of every module documented with KDoc.

    Complex business logic commented with “why”, not “what”.

    Architecture decisions recorded in docs/adr/ (e.g., ADR for choosing Opus over AAC).