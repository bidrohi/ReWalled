pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ReWalled"
include(":app:android")
include(":app:desktop")
include(":shared:core:network")
include(":shared:library")
include(":shared:model")
include(":shared:service:reddit")
include(":shared:wallpaper:cache")
include(":shared:wallpaper:data")
