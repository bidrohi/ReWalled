import com.android.build.api.dsl.CommonExtension
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
    alias(libs.plugins.kotlin.android).apply(false)
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

val jvmVersion = JavaVersion.VERSION_11
val kotlinJvmTarget = JvmTarget.JVM_11

configure(subprojects) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(kotlinJvmTarget)
        }
    }
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = jvmVersion.toString()
        targetCompatibility = jvmVersion.toString()
    }
}

subprojects {
    afterEvaluate {
        (extensions.findByName("kotlin") as? KotlinMultiplatformExtension)?.apply {
            (targets.find {
                it is KotlinMultiplatformAndroidLibraryTarget
            } as? KotlinMultiplatformAndroidLibraryTarget)?.apply {
                compileSdk = 36
                buildToolsVersion = "36.0.0"
                minSdk = 23
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                compilerOptions {
                    jvmTarget.set(kotlinJvmTarget)
                }
            }
        }
        (extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *>)?.apply {
            compileSdk = 36
            buildToolsVersion = "36.0.0"
            defaultConfig {
                minSdk = 23
            }
            compileOptions {
                sourceCompatibility = jvmVersion
                targetCompatibility = jvmVersion
                isCoreLibraryDesugaringEnabled = true
            }
            buildFeatures {
                buildConfig = true
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
            dependencies {
                "coreLibraryDesugaring"(libs.jdk.desugar)
            }
        }
    }
}
