import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.konan.properties.hasProperty

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.triplet.play")
}

val localProperties = gradleLocalProperties(rootDir)
val hasKeystore = localProperties.hasProperty("signing.keystore")
val keystoreFile: String by lazy { localProperties.getProperty("signing.keystore") }
val keystoreAlias: String by lazy { localProperties.getProperty("signing.alias") }
val keystorePassword: String by lazy { localProperties.getProperty("signing.password") }
val hasPlayPublisherKey = localProperties.hasProperty("play.publisherKey")
val playPublisherKeyFile: String by lazy { localProperties.getProperty("play.publisherKey") }

android {
    namespace = "com.bidyut.tech.rewalled"
    compileSdk = Versions.Sdk.Compile
    buildToolsVersion = Versions.Sdk.BuildTools
    defaultConfig {
        applicationId = "com.bidyut.tech.rewalled"
        minSdk = Versions.Sdk.Min
        targetSdk = Versions.Sdk.Target
        versionCode = AppVersion.code
        versionName = AppVersion.name
    }
    compileOptions {
        sourceCompatibility = Versions.Jvm.Compatibility
        targetCompatibility = Versions.Jvm.Compatibility
    }
    kotlinOptions {
        jvmTarget = Versions.Jvm.Target
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Kotlin.CompilerExt
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        if (hasKeystore) {
            create("release") {
                storeFile = file(keystoreFile)
                storePassword = keystorePassword
                keyAlias = keystoreAlias
                keyPassword = keystorePassword
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}

if (hasPlayPublisherKey) {
    play {
        serviceAccountCredentials.set(file(playPublisherKeyFile))
        defaultToAppBundles.set(true)
    }
}

dependencies {
    implementation(project(":shared:library"))
    implementation(project(":shared:model"))
    implementation(project(":shared:wallpaper:data"))

    val composeBom = platform(Deps.AndroidX.Compose.Bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(Deps.AndroidX.Compose.Ui)
    implementation(Deps.AndroidX.Compose.Foundation)
    implementation(Deps.AndroidX.Compose.UiGraphids)
    debugImplementation(Deps.AndroidX.Compose.Debug.Tooling)
    debugImplementation(Deps.AndroidX.Compose.Debug.Preview)
    implementation(Deps.AndroidX.Compose.Material3)
    implementation(Deps.AndroidX.Activity)
    implementation(Deps.AndroidX.Lifecycle)
    implementation(Deps.AndroidX.Navigation)

    implementation(Deps.Coil)

    implementation(Deps.FluentIcons)
}
