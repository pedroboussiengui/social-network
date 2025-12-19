package org.example.infra.http

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.example.application.usecase.profile.CreateProfile
import org.example.application.usecase.profile.CreateProfileRequest
import org.example.application.usecase.profile.DeleteProfile
import org.example.application.usecase.profile.UploadAvatarProfile
import org.koin.ktor.ext.inject

fun Application.profileModule() {
    val createProfile by inject<CreateProfile>()
    val deleteProfile by inject<DeleteProfile>()
    val uploadAvatarProfile by inject<UploadAvatarProfile>()

    routing {
        route("/profiles") {
            post {
                val request = call.receive<CreateProfileRequest>()
                val response = createProfile.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
            post("/{profileId}/avatar") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val fileName = part.originalFileName ?: "file"
                        val fileBytes = part.provider().readRemaining().readByteArray()
                        val request = UploadAvatarProfile.Input(
                            profileId = profileId,
                            fileName = fileName,
                            fileBytes = fileBytes
                        )
                        uploadAvatarProfile.execute(request)
                    }
                    part.dispose() // clea up resources
                }
                call.respond(HttpStatusCode.OK, "Avatar uploaded successfully")
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


//val fileBytes = part.streamProvider().readBytes()
// Here you would typically save the file, e.g., to a file system or cloud storage
// For demonstration, we'll just acknowledge receipt
//call.application.environment.log.info("Received avatar for profile $profileId: $filename (${fileBytes.size} bytes)")