package org.example.infra.http

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.helloModule() {
    routing {
        get("/hello") {
            call.respondText("Hello from Ktor!")
        }
    }
}