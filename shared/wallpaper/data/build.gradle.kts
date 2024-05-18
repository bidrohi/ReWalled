plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:model"))
            implementation(project(":shared:service:reddit"))
            implementation(project(":shared:wallpaper:cache"))
            implementation(libs.kotlin.serialization.json)
            implementation(libs.kotlin.coroutines)
            api(libs.store5)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.bidyut.tech.rewalled.data"
}
