package org.example

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.example.infra.http.helloModule

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::helloModule)
        .start(wait = true)
}
