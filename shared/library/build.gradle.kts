plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ReWalledUI"
            isStatic = true
            freeCompilerArgs += listOf("-Xoverride-konan-properties=minVersion.ios=14.0")
            linkerOpts.add("-lsqlite3")

            export(libs.nsexception)
            export(project(":shared:model"))
        }
    }
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:model"))
            implementation(project(":shared:service:reddit"))
            implementation(project(":shared:wallpaper:cache"))
            implementation(project(":shared:wallpaper:data"))
            implementation(project(":shared:core:network"))

            implementation(libs.kotlin.coroutines.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.encoding)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.json)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation(libs.jetbrains.lifecycle)
            implementation(libs.jetbrains.navigation)
            implementation(libs.jetbrains.material3.adaptive.core)
            implementation(libs.jetbrains.material3.adaptive.layout)

            implementation(libs.coil.compose)

            implementation(libs.kermit)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.ktor.client.mock)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.activity)
            implementation(libs.kotlin.coroutines.android)

            implementation(libs.coil.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil.ktor)
            api(libs.nsexception)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlin.coroutines.swing)

            implementation(libs.coil.okhttp)
        }
    }
}

android {
    namespace = "com.bidyut.tech.rewalled.shared"
}
