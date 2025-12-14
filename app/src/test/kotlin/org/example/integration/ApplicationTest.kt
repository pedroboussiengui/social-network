package org.example.integration

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
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
}