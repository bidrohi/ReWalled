import org.gradle.api.JavaVersion

object Versions {
    object Sdk {
        const val Min = 21
        const val Target = 33
        const val Compile = 33
        const val BuildTools = "33.0.2"
    }

    object Jvm {
        const val Target = "17"
        val Compatibility = JavaVersion.VERSION_17
    }
}
