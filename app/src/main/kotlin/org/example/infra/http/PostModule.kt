package org.example.infra.http

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.example.application.usecase.CreatePost
import org.example.application.usecase.CreatePostRequest
import org.example.infra.database.DatabaseConnection
import org.example.infra.database.PostModel
import org.example.infra.database.PostgresPostRepository
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.ktor.ext.inject

fun Application.postModule() {
    val dbJdbcUrl = environment.config.property("database.jdbcUrl").getString()
    val dbUsername = environment.config.property("database.username").getString()
    val dbPassword = environment.config.property("database.password").getString()

    DatabaseConnection.init(dbJdbcUrl, dbUsername, dbPassword)

    transaction {
        SchemaUtils.create(PostModel)
    }

    install(ContentNegotiation) {
        json()
    }

    val createPost by inject<CreatePost>()

    routing {
        post("/posts") {
            val request = call.receive<CreatePostRequest>()
            val response = createPost.execute(request)
            call.respond(HttpStatusCode.Created, response)
        }
    }
}