plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version(Versions.Sdk.GradlePlugin).apply(false)
    id("com.android.library").version(Versions.Sdk.GradlePlugin).apply(false)
    kotlin("android").version(Versions.Kotlin.Core).apply(false)
    kotlin("multiplatform").version(Versions.Kotlin.Core).apply(false)
    kotlin("plugin.serialization").version(Versions.Kotlin.Core).apply(false)
    id("com.google.devtools.ksp").version(Versions.Kotlin.Ksp).apply(false)
    id("app.cash.sqldelight").version(Versions.SqlDelight).apply(false)
    id("com.github.triplet.play").version(Versions.PlayPublisher).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    buildDir = File("${rootDir}/build/${projectDir.relativeTo(rootDir)}")
}
