package org.example.integration

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.example.application.usecase.CommentPostRequest
import org.example.application.usecase.CommentPostResponse
import org.example.application.usecase.CreatePostRequest
import org.example.application.usecase.CreatePostResponse
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.example.module
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CommentIntegrationTest {

    val postgresContainer = PostgreSQLContainer("postgres:16-alpine").apply {
        withDatabaseName("test")
        withUsername("test")
        withPassword("test")
        start()
    }

    @Test
    fun `should make a request to create new post successfully`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "database.jdbcUrl" to postgresContainer.jdbcUrl,
                "database.username" to postgresContainer.username,
                "database.password" to postgresContainer.password
            )
        }
        application {
            module()
        }
        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val createPostRequest = CreatePostRequest(
            authorId = UUID.randomUUID(),
            postType = PostType.TEXT,
            content = "This is a test post",
            description = "This is a test description",
            postVisibility = PostVisibility.PUBLIC,
            hashTags = listOf("test", "ktor"),
            allowComments = true
        )
        val responseCreatePostBody = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(createPostRequest)
        }.body<CreatePostResponse>()

        val commentPostRequest = CommentPostRequest(
            authorId = UUID.randomUUID(),
            content = "This is a test comment",
            null
        )
        val responseCommentPostRequest = client.post("/posts/${responseCreatePostBody.id}/comment") {
            contentType(ContentType.Application.Json)
            setBody(commentPostRequest)
        }
        val responseCommentPostBody = responseCommentPostRequest.body<CommentPostResponse>()

        assertEquals(HttpStatusCode.Created, responseCommentPostRequest.status)
        assertNotNull(responseCommentPostBody.id)
        assertEquals(commentPostRequest.authorId, responseCommentPostBody.authorId)
        assertEquals(commentPostRequest.postId, responseCommentPostBody.postId)
        assertEquals(commentPostRequest.content, responseCommentPostBody.content)
        assertEquals(commentPostRequest.parentCommentId, responseCommentPostBody.parentCommentId)
        assertNotNull(responseCommentPostBody.createdAt)
    }

}