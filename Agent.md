# Agent.md - Developer Guidelines for ReWalled

## Build Commands
- Full build: `./gradlew build`
- Android app: `./gradlew assembleDebug` or `./gradlew assembleRelease`
- Desktop app: `./gradlew packageDistributionForCurrentOS`

## Test Commands
- Run all tests: `./gradlew allTests`
- Run specific test: `./gradlew :shared:library:testDebugUnitTest --tests "com.bidyut.tech.rewalled.YourTestClass.testMethod"`
- Run tests for specific module: `./gradlew :shared:library:test`

## Lint/Formatting
- Kotlin formatting: Uses default Kotlin conventions with Ktor and Compose
- No specific lint command configured - relies on IDE inspection

## Code Style Guidelines

### Imports
- Group imports by standard Java/Kotlin, Android, third-party, and project imports
- No wildcard imports except for generated code
- Alphabetize within groups

### Formatting
- Kotlin standard formatting
- 4 spaces for indentation (no tabs)
- Max line width: 120 characters

### Naming Conventions
- Classes: PascalCase (e.g., CategoriesViewModel)
- Functions: camelCase (e.g., isDynamicColorSupported)
- Variables: camelCase (e.g., colorScheme)
- Constants: UPPER_SNAKE_CASE when in companion objects or at file level

### Types
- Prefer val over var when possible
- Use explicit types for public APIs
- Leverage Kotlin's type inference for local variables

### Error Handling
- Use Kotlin coroutines for async operations
- Handle errors with try/catch blocks appropriately
- Use sealed classes for representing state with success/error variants
- Log errors with Kermit logger (already integrated)

### Architecture
- Multiplatform Kotlin with shared common code
- MVVM pattern with ViewModels
- Compose for UI
- Dependency injection via AppGraph

### Platform-specific Code
- Use expect/actual for platform-specific implementations
- Keep platform-specific code in separate source sets (androidMain, iosMain, desktopMain)

## Dependencies
- Kotlin Coroutines for async
- Ktor for networking
- Compose Multiplatform for UI
- SQLDelight for database
- Firebase for analytics/crash reporting
