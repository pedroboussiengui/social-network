package org.example.infra.http

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.application.usecase.CreatePost
import org.example.application.usecase.CreatePostRequest
import org.example.infra.database.DatabaseConnection
import org.example.infra.database.PostModel
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