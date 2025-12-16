package org.example.infra.http

import io.ktor.http.Parameters
import java.util.UUID

fun Parameters.uuid(input: String): UUID? =
    this[input]?.let {
        try {
            UUID.fromString(it)
        } catch (_: IllegalArgumentException) {
            null
        }
    }