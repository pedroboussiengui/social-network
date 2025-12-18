package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.CreateProfile
import org.example.application.usecase.CreateProfileRequest
import org.koin.ktor.ext.inject

fun Application.profileModule() {
    val createProfile by inject<CreateProfile>()

    routing {
        route("/profiles") {
            post {
                val request = call.receive<CreateProfileRequest>()
                val response = createProfile.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}