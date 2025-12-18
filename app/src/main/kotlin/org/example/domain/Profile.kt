package org.example.domain

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class Profile(
    val id: UUID,
    val username: String,
    val displayName: String,
    val avatar: String?,
    val description: String?,
    val status: ProfileStatus,
    val visibility: ProfileVisibility,
    val telephone: String?,
    val email: String,
    val birthDate: LocalDate,
    val region: String,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val deletedAt: Instant?
) {
    companion object {
        fun create(
            username: String,
            displayName: String,
            avatar: String,
            description: String,
            telephone: String?,
            email: String,
            birthDate: LocalDate,
            region: String
        ): Profile {
            return Profile(
                id = UUID.randomUUID(),
                username = username,
                displayName = displayName,
                avatar = avatar,
                description = description,
                status = ProfileStatus.ACTIVE,
                visibility = ProfileVisibility.PUBLIC,
                telephone = telephone,
                email = email,
                birthDate = birthDate,
                region = region,
                createdAt = Instant.now(),
                updatedAt = null,
                deletedAt = null
            )
        }
    }
}


enum class ProfileStatus {
    ACTIVE, SUSPEND, BANNED
}

enum class ProfileVisibility {
    PUBLIC, PRIVATE
}