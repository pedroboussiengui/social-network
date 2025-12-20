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
import org.example.application.usecase.profile.GetProfileByUsername
import org.example.application.usecase.profile.ListProfilePosts
import org.example.application.usecase.profile.PrivateProfile
import org.example.application.usecase.profile.PublicProfile
import org.example.application.usecase.profile.ToggleFollowProfile
import org.example.application.usecase.profile.UploadAvatarProfile
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.profileModule() {
    val createProfile by inject<CreateProfile>()
    val deleteProfile by inject<DeleteProfile>()
    val uploadAvatarProfile by inject<UploadAvatarProfile>()
    val getProfileByUsername by inject<GetProfileByUsername>()
    val toggleFollowProfile by inject<ToggleFollowProfile>()
    val listProfilePosts by inject<ListProfilePosts>()
    val publicProfile by inject<PublicProfile>()
    val privateProfile by inject<PrivateProfile>()

    routing {
        route("/profiles") {
            post {
                val request = call.receive<CreateProfileRequest>()
                val response = createProfile.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
            get {
                val username = call.request.queryParameters["username"]
                    ?: throw BadRequestException("Missing username")
                val request = GetProfileByUsername.Input(username = username)
                val response = getProfileByUsername.execute(request)
                call.respond(HttpStatusCode.OK, response)
            }
            post("/{profileId}/avatar") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        // sanitize input e.g. ../
                        val fileName = part.originalFileName ?: "file"
                        // use ByteReadChannel for large files
                        val fileBytes = part.provider().readRemaining().readByteArray()
                        // MIME content type instead extension
                        val extension = part.contentType
                        val request = UploadAvatarProfile.Input(
                            principal = principal,
                            profileId = profileId,
                            fileName = fileName,
                            fileBytes = fileBytes
                        )
                        uploadAvatarProfile.execute(request)
                    }
                    part.dispose() // clea up resources
                }
                call.respond(HttpStatusCode.Created, "Avatar uploaded successfully")
            }
            delete("/{profileId}") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val request = DeleteProfile.Input(
                    principal = principal,
                    profileId = profileId
                )
                deleteProfile.execute(request)
                call.respond(HttpStatusCode.NoContent)
            }
            get("/{profileId}/posts") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val input = ListProfilePosts.Input(
                    profileId = profileId,
                    principal = principal
                )
                val response = listProfilePosts.execute(input)
                call.respond(HttpStatusCode.OK, response)
            }
            post("/{profileId}/toggle-follow") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val input = ToggleFollowProfile.Input(
                    followerId = principal,
                    followedId = profileId
                )
                toggleFollowProfile.execute(input)
                call.respond(HttpStatusCode.OK, "Follow status toggled successfully")
            }
            patch("/{profileId}/public") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val input = PublicProfile.Input(
                    principal = principal,
                    profileId = profileId
                )
                publicProfile.execute(input)
                call.respond(HttpStatusCode.OK, "Profile set to public")
            }
            patch("/{profileId}/private") {
                val profileId = call.parameters.uuid("profileId")
                    ?: throw BadRequestException("Missing profile ID")
                val principal = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val input = PrivateProfile.Input(
                    principal = principal,
                    profileId = profileId
                )
                privateProfile.execute(input)
                call.respond(HttpStatusCode.OK, "Profile set to private")
            }
        }
    }
}