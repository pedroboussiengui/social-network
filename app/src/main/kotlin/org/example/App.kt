package org.example

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.EngineMain
import io.ktor.server.netty.Netty
import org.example.infra.http.helloModule
import org.example.infra.http.postModule

//fun main() {
//    embeddedServer(Netty, port = 8080, module = Application::helloModule)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    helloModule()
    postModule()
}