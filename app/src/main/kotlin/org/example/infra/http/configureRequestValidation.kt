package org.example.infra.http

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import org.example.application.usecase.profile.CreateProfileRequest
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import java.time.LocalDate
import java.util.Locale

fun CreateProfileRequest.validate() {
    validate(this) {
        validate(CreateProfileRequest::username).isNotNull().isNotEmpty().startsWith("@").hasSize(min = 3, max = 20)
        validate(CreateProfileRequest::displayName).isNotNull().isNotEmpty().hasSize(min = 3, max = 20)
        validate(CreateProfileRequest::description).isNotNull().isNotEmpty()
        validate(CreateProfileRequest::telephone).hasSize(min = 10, max = 15)
        validate(CreateProfileRequest::email).isNotNull().isEmail()
        validate(CreateProfileRequest::birthDate).isNotNull().isLessThan(LocalDate.now())
        validate(CreateProfileRequest::region).isNotNull().isIn(validRegions)
    }
}

val validRegions = listOf("US", "CA", "GB", "AU", "DE", "FR", "ES", "IT", "BR", "IN", "JP", "CN")

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<CreateProfileRequest> { request ->
            try {
                request.validate()
                ValidationResult.Valid
            } catch (e: ConstraintViolationException) {
                ValidationResult.Invalid(validationErrors(e))
            }
        }
    }
}

fun validationErrors(e: ConstraintViolationException): List<String> =
    e.constraintViolations
        .mapToMessage(baseName = "messages", locale = Locale.ENGLISH)
        .map { "${it.property}: ${it.message}" }