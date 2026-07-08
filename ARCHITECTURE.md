# Architecture Overview

## 1. High‑Level Structure
The app follows **clean architecture** with a strict separation of concerns:

- **Presentation Layer** (Jetpack Compose UI + ViewModels)
- **Domain Layer** (pure Kotlin use cases, no Android dependencies)
- **Data Layer** (repositories, local & remote data sources)
- **Platform Layer** (Android telephony/audio services, permissions, system integration)

Data flows **unidirectionally**: UI emits `Intent` → ViewModel → Use Cases → Repository → Data Source. State flows back as `StateFlow` from ViewModel to Compose.

## 2. Module Structure (Gradle)
:app
:core:ui – custom design system, animation engine, theme
:core:common – shared utilities, DI annotations
:core:domain – pure domain logic (use cases, models)
:core:data – repositories, data sources, database, file storage
:core:telecom – InCallService, ConnectionService, TelecomManager
:core:recording – audio capture, processing, encryption
:feature:dialer – dialpad, smart search, call initiation
:feature:contacts – contacts list, favorites, details
:feature:recents – call history, swipe actions
:feature:incall – in-call UI, call control, recording toggle
:feature:recordings – playback, management, sharing

Dependencies: `feature` → `core:domain`, `core:data`, `core:ui`; `core:recording` → `core:data`; no feature depends on another feature. DI framework (Hilt) wires everything.

## 3. Presentation Layer

### UI – Jetpack Compose
- **100% Compose UI**, no Fragments/XML. `ComponentActivity` hosts one `NavHost` or a custom backstack.
- **Design System:** All components come from `core:ui`. No Material imports. `DialerTheme` provides colors, typography, shapes.
- **Screens:** Each screen is a composable that receives a `state` and `event` lambdas from its ViewModel.
- **Navigation:** Type‑safe navigation with serializable routes. Shared element transitions are handled via a custom `TransitionScope` and `SharedTransitionLayout`.

### State Management
- **UDF (MVI‑like) pattern:** `StateFlow<ScreenState>` exposed by ViewModel, `Channel<UiEvent>` for one‑shot events (e.g., snackbar).
- `ScreenState` is an immutable data class containing all UI‑needed data.
- ViewModels use `SavedStateHandle` for process death survival (active call restoration).
- `collectAsStateWithLifecycle()` for lifecycle‑aware collection.

### Animation Engine
- A dedicated `AnimationEngine` object inside `core:ui` provides `spring`, `tween`, and `infinite` specs.
- Complex transitions (e.g., dialpad to in‑call) use `AnimatedContent` and `graphicsLayer` transformations orchestrated by a `TransitionManager`.

## 4. Domain Layer
Contains **use cases** – each a single responsibility class (invoke operator) with zero Android imports.

### Core Use Cases
- `DialNumberUseCase` – validates, formats, and initiates call via platform bridge.
- `SearchContactsUseCase` – in‑memory T9/trie search across contacts and recents.
- `ObserveActiveCallUseCase` – returns a flow of current call state (idle, dialing, active, etc.).
- `ManageRecordingUseCase` – starts/stops recording with compliance checks.
- `GetRecordingsUseCase`, `DeleteRecordingUseCase`, `ShareRecordingUseCase` – playback, delete, export.
- `CheckRecordingLegalityUseCase` – determines if recording is allowed based on region/contact/settings.

### Domain Models
- `Contact`, `RecentCall`, `CallState`, `Recording`, `ComplianceRule` – pure Kotlin data classes, decoupled from DTOs.

## 5. Data Layer

### Repositories
- `ContactRepository` – wraps system `ContactsContract` and user favorites.
- `CallLogRepository` – wraps `CallLog.Calls` content provider, merges with local metadata (tags, recording links).
- `RecordingRepository` – manages encrypted audio files and their metadata in local database.

### Local Data Sources
- **Room database** (with SQLCipher for encryption) stores recordings metadata, user settings, favorites.
- **FileStore** – encrypted directory (`EncryptedFile` / `FileLocker`) for audio files.
- **SharedPreferences** (DataStore) for simple flags, but most config in DB.

### Audio Capture & Call Integration
- **CallAudioCaptureService** – foreground service using `AudioPlaybackCapture` API (or `MediaProjection` if necessary) to capture both uplink/downlink streams.
- **CallRecordingManager** – controls the capture lifecycle, encodes to Opus in real time, encrypts stream before writing to file.
- **ComplianceEngine** – checks rules before recording starts; injects periodic beep/tone (via `AudioTrack`) if required by law.

## 6. Platform / Telecom Integration
- **InCallService + ConnectionService** – replaces the stock call UI. The app must be set as default dialer to show custom in‑call screen.
- **TelecomManager** – used to place calls, manage ongoing call, and receive callbacks.
- **PhoneStateListener** – to monitor telephony state changes (fallback if not using InCallService).
- **Permissions:** Runtime requests for `RECORD_AUDIO`, `READ_CONTACTS`, `READ_CALL_LOG`, `MANAGE_OWN_CALLS`, etc.

## 7. Call Recording Flow (Detailed)
1. User presses record (manual) or auto‑record rule triggers.
2. `ManageRecordingUseCase` asks `ComplianceEngine` for legality; if allowed, emits `RecordingStarted` event.
3. In‑call UI shows visual indicator, playback of mandatory beep (if needed).
4. `CallRecordingManager` starts capturing audio via `AudioPlaybackCapture` configuration (allowed usage = voice communication + usage hints).
5. Audio frames are compressed to Opus using `MediaCodec` in an async pipeline.
6. Encrypted chunks written to `FileStore`, metadata saved to DB with call details.
7. When call ends or user stops, file finalised, encryption key sealed, and a notification triggers playback shortcut.
8. Recording appears in “Recordings” tab, playable with a custom waveform view (decoding on‑the‑fly).

## 8. Dependency Injection
- **Hilt** throughout. Modules: `DataModule`, `DomainModule`, `TelecomModule`, `RecordingModule`.
- Interfaces for all platform‑specific classes, so the domain layer remains testable.

## 9. Testing Strategy
- **Unit tests** for domain use cases (pure JUnit).
- **ViewModel tests** with faked repositories and `MainDispatcherRule`.
- **UI tests** using Compose testing library with mocked ViewModels (screenshot tests for design consistency).
- **Integration tests** for recording pipeline using a fake `AudioRecord` source.