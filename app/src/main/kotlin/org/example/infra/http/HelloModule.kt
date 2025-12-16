package org.example.infra.http

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.helloModule() {
    val env = environment.config.propertyOrNull("ktor.environment")?.getString()
    routing {
        get("/hello") {
            call.respondText("Hello from Ktor!")
        }
        get("/env") {
            when(env) {
                "dev" -> call.respondText("Development Environment")
                "prod" -> call.respondText("Production Environment")
                else -> call.respondText("Unknown Environment")
            }
        }
    }
}