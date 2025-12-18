package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.profile.CreateProfile
import org.example.application.usecase.profile.CreateProfileRequest
import org.example.application.usecase.profile.DeleteProfile
import org.koin.ktor.ext.inject

fun Application.profileModule() {
    val createProfile by inject<CreateProfile>()
    val deleteProfile by inject<DeleteProfile>()

    routing {
        route("/profiles") {
            post {
                val request = call.receive<CreateProfileRequest>()
                val response = createProfile.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
            delete("/{profileId}") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                deleteProfile.execute(profileId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}