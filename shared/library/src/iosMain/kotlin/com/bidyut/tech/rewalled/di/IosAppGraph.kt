package com.bidyut.tech.rewalled.di

import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
class IosAppGraph(
    @param:ObjCName("_")
    override val coordinator: PlatformCoordinator,
    enableDebug: Boolean = false,
) : AppGraph(enableDebug) {

    override val database by lazy {
        Database(DatabaseDriverFactory())
    }

    override val httpClient by lazy {
        HttpClient(Darwin) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            baseConfiguration(enableDebug)
        }
    }
}
