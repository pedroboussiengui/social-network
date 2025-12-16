package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.CreatePost
import org.example.application.usecase.CreatePostRequest
import org.example.application.usecase.FindPostById
import org.koin.ktor.ext.inject

fun Application.postModule() {
    val createPost by inject<CreatePost>()
    val findPostById by inject<FindPostById>()

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
        }
    }
}