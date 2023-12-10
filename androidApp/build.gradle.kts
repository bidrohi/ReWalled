import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.playPublisher)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = gradleLocalProperties(rootDir)
val hasKeystore = localProperties.hasProperty("signing.keystore")
val keystoreFile: String by lazy { localProperties.getProperty("signing.keystore") }
val keystoreAlias: String by lazy { localProperties.getProperty("signing.alias") }
val keystorePassword: String by lazy { localProperties.getProperty("signing.password") }
val hasPlayPublisherKey = localProperties.hasProperty("play.publisherKey")
val playPublisherKeyFile: String by lazy { localProperties.getProperty("play.publisherKey") }

object AppVersion {
    private val now by lazy {
        OffsetDateTime.now(ZoneOffset.UTC)
    }

    val code by lazy {
        DateTimeFormatter.ofPattern("yyMMddHH")
            .format(now)
            .toInt()
    }

    val name by lazy {
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .format(now)
    }
}

android {
    namespace = "com.bidyut.tech.rewalled"
    defaultConfig {
        applicationId = "com.bidyut.tech.rewalled"
        targetSdk = 34
        versionCode = AppVersion.code
        versionName = AppVersion.name
    }
    buildFeatures {
        buildConfig = true
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
        getByName("debug" ) {
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
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

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
