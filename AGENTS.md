# ReWalled

ReWalled is a cross-platform wallpaper application that fetches images from Reddit. It is built using Kotlin Multiplatform (KMP), enabling a shared codebase for Android, iOS, and Desktop applications.

## Project Structure

The project is organized into a modular structure:

*   **app/**: Contains the Android application entry point and configuration.
*   **shared/**: The core multiplatform code, further divided into:
    *   `core/network`: Networking infrastructure (using Ktor).
    *   `library`: General shared logic and platform-specific implementations.
    *   `model`: Shared data models.
    *   `service/reddit`: Specific service for interacting with the Reddit API.
    *   `wallpaper/`: Contains wallpaper-related logic (`cache` for database, `data` for repositories).
*   **desktopMain/**: (Implicit in shared/app) Contains Desktop-specific entry points.
*   **iosApp/**: (Inside `app/ios` or at root) Contains the iOS application project (Swift/Xcode).

## Technology Stack

*   **Language:** Kotlin (Multiplatform)
*   **UI Framework:** Compose Multiplatform (Jetpack Compose)
*   **Build System:** Gradle (Kotlin DSL) with Version Catalogs (`libs.versions.toml`)
*   **Networking:** Ktor
*   **Database:** SQLDelight
*   **Image Loading:** Coil
*   **Async:** Kotlin Coroutines
*   **Logging:** Kermit
*   **Analytics/Crash:** Firebase (Android)

## Building and Running

### Prerequisites
*   JDK 11 or higher
*   Android SDK (for Android build)
*   Xcode (for iOS build, macOS only)

### Build Commands
*   **Full Build:**
    ```bash
    ./gradlew build
    ```
*   **Android App:**
    ```bash
    ./gradlew :app:assembleDebug
    ```
*   **Desktop App:**
    ```bash
    ./gradlew packageDistributionForCurrentOS
    ```

### Testing
*   **Run All Tests:**
    ```bash
    ./gradlew allTests
    ```
*   **Run Specific Test Module:**
    ```bash
    ./gradlew :shared:library:test
    ```

## Development Conventions

### Code Style
*   **Formatting:** Follows standard Kotlin conventions (4 spaces indentation, 120 char line limit).
*   **Naming:**
    *   Classes: `PascalCase`
    *   Functions/Variables: `camelCase`
    *   Constants: `UPPER_SNAKE_CASE`
*   **Imports:** Grouped by Java/Kotlin, Android, Third-party, Project. No wildcard imports.

### Architecture
*   **Pattern:** MVVM (Model-View-ViewModel).
*   **State Management:** ViewModels expose state via Flows/StateFlows to Compose UI.
*   **Dependency Injection:** Uses a manual `AppGraph` or similar service locator pattern.
*   **Platform Specifics:** Uses `expect`/`actual` mechanism. Platform implementations reside in `androidMain`, `iosMain`, and `desktopMain`.

### Error Handling
*   Use `try/catch` within Coroutines.
*   Represent UI state with sealed classes (Success/Error/Loading).
*   Log exceptions using `Kermit`.
