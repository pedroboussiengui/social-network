package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.postModule() {
    val createPost by inject<CreatePost>()
    val findPostById by inject<FindPostById>()
    val commentPost by inject<CommentPost>()
    val toggleLikePost by inject<ToggleLikePost>()

    routing {
        route("/posts") {
            get("/{postId}") {
                val postId = call.parameters.uuid("postId")
                val response = findPostById.execute(postId!!)
                call.respond(HttpStatusCode.OK, response)
            }
            post {
                val request = call.receive<CreatePostRequest>()
                val response = createPost.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
            post("/{postId}/toggle-like") {
                val postId = call.parameters.uuid("postId")
                val profileId = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val request = LikePostRequest(profileId, postId!!)
                toggleLikePost.execute(request)
                call.respond(HttpStatusCode.NoContent)
            }
            post("/{postId}/comment") {
                val request = call.receive<CommentPostRequest>()
                val profileId = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val postId = call.parameters.uuid("postId")
                    ?: throw IllegalArgumentException("Post ID is missing")
                val input = CommentPost.Input(
                    postId = postId,
                    profileId = profileId,
                    request = request
                )
                val response = commentPost.execute(input)
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}