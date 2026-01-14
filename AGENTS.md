# ReWalled

ReWalled is a cross-platform wallpaper application that fetches images from Reddit. Built with Kotlin Multiplatform (KMP) for Android, iOS, and Desktop.

## Project Structure

- **app/android**: Android application entry point
- **app/desktop**: Desktop application entry point
- **shared/core/network**: Ktor networking infrastructure
- **shared/library**: Shared UI and platform-specific implementations
- **shared/model**: Shared data models
- **shared/service/reddit**: Reddit API service
- **shared/wallpaper/cache**: SQLDelight database
- **shared/wallpaper/data**: Repositories

## Build Commands

### Full Build
```bash
./gradlew build
```

### Platform Builds
- **Android APK**: `./gradlew :app:android:assembleDebug`
- **Desktop DMG/AppImage**: `./gradlew :app:desktop:packageDistributionForCurrentOS`
- **Android Release**: `./gradlew :app:android:assembleRelease`

### Testing
- **Run all tests**: `./gradlew allTests`
- **Run single test**: `./gradlew :shared:library:test --tests "*TestClass.testMethod*"`
- **Module-specific tests**: `./gradlew :shared:library:allTests`
- **Android connected tests**: `./gradlew :app:android:connectedAndroidTest`
- **Clean test reports**: `./gradlew cleanAllTests`

### Code Quality
- **Lint (Android)**: `./gradlew lint`
- **Clean build**: `./gradlew clean`

## Code Style

### Formatting
- 4 spaces indentation, 120 character line limit
- No trailing whitespace, blank line at EOF

### Imports (no wildcards, in order)
1. `kotlin.*`
2. `android.*`, `androidx.*`
3. Compose: `androidx.compose.*`, `coil3.*`
4. Third-party: `io.ktor.*`, `co.touchlab.kermit.*`, `app.cash.sqldelight.*`
5. Project: `com.bidyut.tech.rewalled.*`

### Naming Conventions
- **Classes**: `PascalCase` (e.g., `SubRedditViewModel`, `RedditService`)
- **Functions/Variables**: `camelCase` (e.g., `getWallpaperFeed`, `uiState`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `BASE_URL`)
- **Type aliases**: `PascalCase` ending (e.g., `SubredditFeedId`)

### Kotlin Conventions

**Data Classes**
- Use for all domain models: `Wallpaper`, `Filter`, `ImageDetail`, `SubredditFeed`
- Implement `toString()`, `equals()`, `hashCode()` automatically

**Sealed Interfaces**
- UI state representation: `UiState.Loading`, `UiState.Error`, `UiState.ShowContent(feed)`
- Route definitions: `Route.Grid(subreddit)`, `Route.Wallpaper(wallpaper)`

**Type Aliases**
- Semantic clarity: `typealias SubredditFeedId = String`

**Extension Functions**
- Domain logic: `SubredditFeedId.dissolve(): Pair<String, Filter>`

**Documentation**
- Kotlin doc comments: `/** ... */` for public APIs
- Describe parameters and return values

### Compose UI (Jetpack Guidelines)
- Use Material3 components: `Material3.Surface`, `Material3.Card`
- Adaptive layouts: `adaptive-layout`, `adaptive-navigator`
- Unidirectional data flow: ViewModel -> State -> Composable
- Keep composables stateless; hoist state to ViewModel
- Design system will be added in future iterations

## Architecture

### MVVM Pattern
- ViewModels extend `androidx.lifecycle.ViewModel`
- Expose state via `Flow<UiState>` or `State<T>`
- Factory pattern: `SubRedditViewModel.factory()`

### Repository Pattern
- Bhandar library: `cached()`, `fetcher`, `storage` properties
- Generic caching with `Bhandar<Request, Response>`

### Dependency Injection
- Manual service locator: `AppGraph.instance`
- Abstract base class with platform implementations
- Components accessed via `appGraph.log`, `appGraph.subredditFeedRepository`

### Platform-Specific Code
- `expect`/`actual` mechanism for cross-platform APIs
- Source sets: `commonMain`, `androidMain`, `iosMain`, `desktopMain`, `jvmMain`
- Platform implementations in respective directories

## Error Handling

### Network Layer
- Return `Result<T>` from service methods
- `try/catch` in coroutines with proper exception mapping
- `IOException` handling for network failures

### Data Layer
- Bhandar's `ReadResult.Loading`, `ReadResult.Data`, `ReadResult.Error`
- Database operations with null safety

### UI Layer
- Sealed interface for state: `Loading`, `Error`, `ShowContent`
- User feedback via UI state, not exceptions

### Logging
- Kermit with tagged loggers: `Logger.withTag("ReWalled")`
- Levels: `log.d()`, `log.e()`, `log.w()`, `log.i()`

## Testing

### Structure
- Common tests: `src/commonTest/kotlin/...`
- Android unit tests: `src/androidUnitTest/kotlin/...`
- Framework: `kotlin.test` (`@Test`, `assertEquals`, `assertTrue`)

### Conventions
- Test files mirror source structure
- Descriptive test names: `testMethodName_condition_expectedResult`
- Use `kotlin.test` assertions

## Technology Stack

- **Language**: Kotlin 2.3.0
- **UI**: Compose Multiplatform 1.9.3 (Material3)
- **Networking**: Ktor 3.3.3
- **Database**: SQLDelight 2.2.1
- **Image Loading**: Coil 3.3.0
- **Async**: Coroutines 1.10.2
- **Logging**: Kermit 2.0.8
- **Caching**: Bhandar 0.4.0
- **Build**: Gradle 8.13.2 with KSP 2.3.4
