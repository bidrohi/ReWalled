import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.jetbrains.compose).apply(false)
    alias(libs.plugins.jetbrains.composeHotReload).apply(false)
    alias(libs.plugins.kotlin.compose).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.playPublisher).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
}

allprojects {
    layout.buildDirectory = File("${rootDir}/build/${projectDir.relativeTo(rootDir)}")
}

object Versions {
    object Android {
        val min = 23
        val target = 37
        val buildTools = "37.0.0"
    }
    val jvmVersion = JavaVersion.VERSION_11
    val kotlinJvmTarget = JvmTarget.JVM_11
}

configure(subprojects) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(Versions.kotlinJvmTarget)
        }
    }
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = Versions.jvmVersion.toString()
        targetCompatibility = Versions.jvmVersion.toString()
    }
}

subprojects {
    afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.apply {
            targets.withType<KotlinMultiplatformAndroidLibraryTarget> {
                minSdk = Versions.Android.min
                compileSdk = Versions.Android.target
                buildToolsVersion = Versions.Android.buildTools
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                compilerOptions {
                    jvmTarget.set(Versions.kotlinJvmTarget)
                }
            }
        }
        extensions.findByType<ApplicationExtension>()?.apply {
            compileSdk = Versions.Android.target
            buildToolsVersion = Versions.Android.buildTools
            defaultConfig {
                minSdk = Versions.Android.min
                targetSdk = Versions.Android.target
            }
            compileOptions {
                sourceCompatibility = Versions.jvmVersion
                targetCompatibility = Versions.jvmVersion
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
