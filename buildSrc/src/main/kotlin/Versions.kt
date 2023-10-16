import org.gradle.api.JavaVersion

object Versions {
    object Sdk {
        const val Min = 21
        const val Target = 34
        const val Compile = 34
        const val BuildTools = "34.0.0"
    }

    object Jvm {
        const val Target = "17"
        val Compatibility = JavaVersion.VERSION_17
    }
}
