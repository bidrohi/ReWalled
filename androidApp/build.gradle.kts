import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.konan.properties.hasProperty

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.playPublisher)
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

    implementation(libs.androidx.activity)
    implementation(libs.precompose.core)
}
