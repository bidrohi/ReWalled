import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

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
        DateTimeFormatter.ofPattern("yy.MM.dd")
            .format(now)
    }
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:library"))
            implementation(libs.kermit)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.bidyut.tech.rewalled.ReWalledKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.bidyut.tech.rewalled"
            packageVersion = AppVersion.name
        }
    }
}
