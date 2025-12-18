package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.*
import org.example.application.usecase.comment.CommentPost
import org.example.application.usecase.comment.CommentPostRequest
import org.example.application.usecase.comment.ListPostComments
import org.example.application.usecase.post.FindPostById
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.postModule() {
    val createPost by inject<org.example.application.usecase.post.CreatePost>()
    val findPostById by inject<FindPostById>()
    val listPostComments by inject<ListPostComments>()
    val commentPost by inject<CommentPost>()
    val toggleLikePost by inject<org.example.application.usecase.post.ToggleLikePost>()

    routing {
        route("/posts") {
            get("/{postId}") {
                val postId = call.parameters.uuid("postId")
                val response = findPostById.execute(postId!!)
                call.respond(HttpStatusCode.OK, response)
            }
            post {
                val request = call.receive<org.example.application.usecase.post.CreatePostRequest>()
                val response = createPost.execute(request)
                call.respond(HttpStatusCode.Created, response)
            }
            post("/{postId}/toggle-like") {
                val postId = call.parameters.uuid("postId")
                val profileId = call.request.headers["X-Profile-ID"]
                    ?. let { UUID.fromString(it) }
                    ?: throw IllegalArgumentException("X-Profile-ID header is missing")
                val request =
                    _root_ide_package_.org.example.application.usecase.post.LikePostRequest(profileId, postId!!)
                toggleLikePost.execute(request)
                call.respond(HttpStatusCode.NoContent)
            }
            get("/{postId}/comments") {
                val postId = call.parameters.uuid("postId")
                    ?: throw IllegalArgumentException("Post ID is missing")
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10
                require(page >= 1) { "page must be >= 1" }
                require(size >= 1) { "size must be >= 1" }
                val pageRequest = PageRequest(page, size)
                val response = listPostComments.execute(pageRequest, postId)
                call.respond(HttpStatusCode.OK, response)
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