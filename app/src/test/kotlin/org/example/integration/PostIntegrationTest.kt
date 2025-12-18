package org.example.integration

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.example.application.usecase.post.CreatePostRequest
import org.example.application.usecase.post.CreatePostResponse
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.example.module
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PostIntegrationTest {

    val postgresContainer = PostgreSQLContainer("postgres:16-alpine").apply {
        withDatabaseName("test")
        withUsername("test")
        withPassword("test")
        start()
    }

    @Test
    fun `should test connection with postgres testContainer`() = testApplication {
        assert(postgresContainer.isRunning)
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
        val response = client.post("/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                CreatePostRequest(
                    authorId = UUID.randomUUID(),
                    postType = PostType.TEXT,
                    content = "This is a test post",
                    description = "This is a test description",
                    postVisibility = PostVisibility.PUBLIC,
                    hashTags = listOf("test", "ktor"),
                    allowComments = true
                )
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val responseBody = response.body<CreatePostResponse>()
        assertNotNull(responseBody.id)
        assertNotNull(responseBody.authorId)
        assertEquals(PostType.TEXT, responseBody.postType)
        assertEquals("This is a test post", responseBody.content)
        assertEquals("This is a test description", responseBody.description)
        assertEquals(PostVisibility.PUBLIC, responseBody.postVisibility)
        assertEquals(listOf("test", "ktor"), responseBody.hashTags)
        assertEquals(true, responseBody.allowComments)
        assertNotNull(responseBody.createdAt)
        assertNull(responseBody.updatedAt)
    }
}