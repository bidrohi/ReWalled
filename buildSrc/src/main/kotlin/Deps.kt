object Deps {
    object Kotlin {
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.Coroutines}"
        const val Datetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlin.Datetime}"
        const val SerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlin.SerializationJson}"
    }

    object Ktor {
        const val SerializationJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor}"

        object Client {
            const val Core = "io.ktor:ktor-client-core:${Versions.Ktor}"
            const val Mock = "io.ktor:ktor-client-mock:${Versions.Ktor}"
            const val Logging = "io.ktor:ktor-client-logging:${Versions.Ktor}"
            const val Encoding = "io.ktor:ktor-client-encoding:${Versions.Ktor}"
            const val ContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.Ktor}"
            const val Android = "io.ktor:ktor-client-android:${Versions.Ktor}"
            const val Darwin = "io.ktor:ktor-client-darwin:${Versions.Ktor}"
        }
    }

    object SqlDelight {
        const val Runtime = "app.cash.sqldelight:runtime:${Versions.SqlDelight}"
        const val Coroutines = "app.cash.sqldelight:coroutines-extensions:${Versions.SqlDelight}"
        const val AndroidDriver = "app.cash.sqldelight:android-driver:${Versions.SqlDelight}"
        const val NativeDriver = "app.cash.sqldelight:native-driver:${Versions.SqlDelight}"
    }

    object AndroidX {
        const val Activity = "androidx.activity:activity-compose:${Versions.AndroidX.ActivityCompose}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.AndroidX.Lifecycle}"
        const val Navigation = "androidx.navigation:navigation-compose:${Versions.AndroidX.Navigation}"

        object Compose {
            const val Bom = "androidx.compose:compose-bom:${Versions.AndroidX.ComposeBom}"
            const val Ui = "androidx.compose.ui:ui"
            const val Foundation = "androidx.compose.foundation:foundation"
            const val UiGraphids = "androidx.compose.ui:ui-graphics"
            const val Material3 = "androidx.compose.material3:material3"

            object Debug {
                const val Tooling = "androidx.compose.ui:ui-tooling"
                const val Preview = "androidx.compose.ui:ui-tooling-preview"
            }
        }
    }

    const val Coil = "io.coil-kt:coil-compose:${Versions.Coil}"
    const val FluentIcons = "com.microsoft.design:fluent-system-icons:${Versions.FluentIcons}@aar"
}
