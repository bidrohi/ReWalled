import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = Versions.Jvm.Target
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ReWalledUI"
            linkerOpts.add("-lsqlite3")
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:model"))
                implementation(project(":shared:service:reddit"))
                implementation(project(":shared:wallpaper:cache"))
                implementation(project(":shared:wallpaper:data"))

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.encoding)
                implementation(libs.ktor.client.contentNavigation)
                implementation(libs.ktor.serialization.json)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.icons.feather)

                implementation(libs.precompose.core)
                implementation(libs.precompose.viewmodel)

                implementation(libs.kamel)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.client.mock)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":shared:core:network"))
                implementation(libs.ktor.client.android)
                implementation(libs.androidx.activity)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(project(":shared:core:network"))
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "com.bidyut.tech.rewalled.shared"
    compileSdk = Versions.Sdk.Compile
    buildToolsVersion = Versions.Sdk.BuildTools
    defaultConfig {
        minSdk = Versions.Sdk.Min
    }
    compileOptions {
        sourceCompatibility = Versions.Jvm.Compatibility
        targetCompatibility = Versions.Jvm.Compatibility
    }
}
