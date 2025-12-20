package org.example.infra.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import org.example.domain.exceptions.EntityNotFoundException
import org.example.domain.exceptions.ProfileAccessDeniedException
import java.time.LocalDateTime

@Serializable
data class Problem(
    val message: String,
    val status: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val errors: List<String>? = null
)

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<EntityNotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                Problem(
                    message = cause.message ?: "Entity not found",
                    status = HttpStatusCode.NotFound.value
                )
            )
        }
        exception<ProfileAccessDeniedException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                Problem(
                    message = cause.message ?: "You are authorized to perform this action",
                    status = HttpStatusCode.Forbidden.value
                )
            )
        }
        exception<RequestValidationException> { call, cause ->
           call.respond(
                HttpStatusCode.BadRequest,
                Problem(
                    message = "Validation failed",
                    status = HttpStatusCode.BadRequest.value,
                    errors = cause.reasons
                )
            )
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respondText(text = "400: ${cause.message}", status = HttpStatusCode.BadRequest)
        }
    }
}
