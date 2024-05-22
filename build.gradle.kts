import com.android.build.api.dsl.CommonExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.compose).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.playPublisher).apply(false)
    alias(libs.plugins.jetbrains.compose).apply(false)
    alias(libs.plugins.google.services).apply(false)
    alias(libs.plugins.firebase.crashlytics).apply(false)
}

allprojects {
    layout.buildDirectory = File("${rootDir}/build/${projectDir.relativeTo(rootDir)}")
}

subprojects {
    val jvmVersion = JavaVersion.VERSION_11
    afterEvaluate {
        (extensions.findByName("kotlinOptions") as? KotlinJvmOptions)?.apply {
            jvmTarget = jvmVersion.toString()
        }
        (extensions.findByName("kotlin") as? KotlinMultiplatformExtension)?.apply {
            androidTarget {
                compilations.all {
                    kotlinOptions {
                        jvmTarget = jvmVersion.toString()
                    }
                }
            }
        }
        (extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *>)?.apply {
            compileSdk = 34
            buildToolsVersion = "34.0.0"
            defaultConfig {
                minSdk = 21
            }
            compileOptions {
                sourceCompatibility = jvmVersion
                targetCompatibility = jvmVersion
            }
            buildFeatures {
                buildConfig = true
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
    }
}
