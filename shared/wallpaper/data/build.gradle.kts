plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android {
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
            baseName = "sharedWallpaperData"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:model"))
                implementation(project(":shared:service:reddit"))
                implementation(project(":shared:wallpaper:cache"))
                implementation(Deps.Kotlin.SerializationJson)
                implementation(Deps.Kotlin.Coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.bidyut.tech.rewalled.data"
    compileSdk = Versions.Sdk.Compile
    buildToolsVersion = Versions.Sdk.BuildTools
    defaultConfig {
        minSdk = Versions.Sdk.Min
        targetSdk = Versions.Sdk.Target
    }
    compileOptions {
        sourceCompatibility = Versions.Jvm.Compatibility
        targetCompatibility = Versions.Jvm.Compatibility
    }
}
