import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.playPublisher)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
}

val localProperties = gradleLocalProperties(rootDir, providers)
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

    val name: String by lazy {
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .format(now)
    }
}

kotlin {
    androidTarget()

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:library"))
            implementation(libs.kermit)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.performance)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
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

compose.desktop {
    application {
        mainClass = "ReWalledKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.bidyut.tech.rewalled"
            packageVersion = "1.0.0"
        }
    }
}

if (hasPlayPublisherKey) {
    play {
        serviceAccountCredentials.set(file(playPublisherKeyFile))
        defaultToAppBundles.set(true)
        track.set("internal")
    }
}
