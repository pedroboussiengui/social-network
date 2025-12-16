package org.example.integration

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import org.example.infra.http.helloModule
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testHello() = testApplication {
        application {
            helloModule()
        }
        val response = client.get("/hello")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello from Ktor!", response.bodyAsText())
    }

    @Test
    fun `should overwrite a env successfully with value dev`() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "dev")
        }
        application {
            helloModule()
        }
        val response = client.get("/env")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Development Environment", response.bodyAsText())
    }

    @Test
    fun `should overwrite a env successfully with value prod`() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "prod")
        }
        application {
            helloModule()
        }
        val response = client.get("/env")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Production Environment", response.bodyAsText())
    }
}