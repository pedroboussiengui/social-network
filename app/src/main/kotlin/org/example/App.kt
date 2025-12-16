package org.example

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import org.example.infra.di.appModule
import org.example.infra.http.helloModule
import org.example.infra.http.postModule
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(Koin) {
        modules(appModule)
    }
    helloModule()
    postModule()
}