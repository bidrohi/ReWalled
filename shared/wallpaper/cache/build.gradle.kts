plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
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
            baseName = "sharedWallpaperCache"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:model"))
                implementation(Deps.Kotlin.Datetime)
                implementation(Deps.Kotlin.SerializationJson)
                implementation(Deps.SqlDelight.Runtime)
                implementation(Deps.SqlDelight.Coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.SqlDelight.AndroidDriver)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Deps.SqlDelight.NativeDriver)
            }
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
    namespace = "com.bidyut.tech.rewalled.cache"
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

sqldelight {
    databases {
        create("RedditDatabase") {
            packageName.set("com.bidyut.tech.rewalled.cache")
        }
    }
}
