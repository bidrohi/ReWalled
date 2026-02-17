plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidLibrary {
        namespace = "com.bidyut.tech.rewalled.data"
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:model"))
            implementation(project(":shared:service:reddit"))
            implementation(project(":shared:wallpaper:cache"))
            implementation(libs.kotlin.serialization.json)
            implementation(libs.kotlin.coroutines.core)
            api(libs.bhandar)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
