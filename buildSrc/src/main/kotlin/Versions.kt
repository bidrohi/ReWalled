import org.gradle.api.JavaVersion

object Versions {
    const val Ktor = "2.2.3"
    const val JUnit = "4.13.2"
    const val Mockk = "1.12.5"
    const val FluentIcons = "1.1.179"
    const val Coil = "2.2.2"
    const val SqlDelight = "2.0.0-alpha05"
    const val PlayPublisher = "3.8.1"

    object Sdk {
        const val Min = 21
        const val Target = 33
        const val Compile = 33
        const val BuildTools = "33.0.2"
        const val GradlePlugin = "8.0.1"
    }

    object Jvm {
        const val Target = "17"
        val Compatibility = JavaVersion.VERSION_17
    }

    object Kotlin {
        const val CompilerExt = "1.4.3"
        const val Core = "1.8.10"
        const val Coroutines = "1.6.4"
        const val SerializationJson = "1.4.1"
        const val Datetime = "0.4.0"
        const val Ksp = "$Core-1.0.9"
    }

    object AndroidX {
        const val Core = "1.9.0"
        const val ComposeBom = "2023.01.00"
        const val ActivityCompose = "1.6.1"
        const val Espresso = "3.4.0"
        const val JUnit = "1.1.3"
        const val Lifecycle = "2.5.1"
        const val Navigation = "2.5.3"
        const val Preference = "1.2.0"
        const val Room = "2.4.3"
    }

    object Firebase {
        const val Bom = "30.3.1"
        const val AppCheck = "16.0.0"
    }

    object PlayServices {
        const val Auth = "20.3.0"
    }
}
