plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
            baseName = "sharedLibrary"
            linkerOpts.add("-lsqlite3")
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:service:reddit"))
                implementation(project(":shared:wallpaper:cache"))
                implementation(project(":shared:wallpaper:data"))
                implementation(Deps.Ktor.Client.Core)
                implementation(Deps.Ktor.Client.Logging)
                implementation(Deps.Ktor.Client.Encoding)
                implementation(Deps.Ktor.Client.ContentNegotiation)
                implementation(Deps.Ktor.SerializationJson)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Deps.Ktor.Client.Mock)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":shared:core:network"))
                implementation(Deps.Ktor.Client.Android)
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
                implementation(project(":shared:core:network"))
                implementation(Deps.Ktor.Client.Darwin)
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
    namespace = "com.bidyut.tech.rewalled.shared"
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
