package org.example.application.usecase.profile

import kotlinx.serialization.Serializable
import org.example.application.port.ProfileRepository
import org.example.domain.Profile
import org.example.domain.ProfileStatus
import org.example.domain.ProfileVisibility
import org.example.infra.http.InstantSerializer
import org.example.infra.http.LocalDateSerializer
import org.example.infra.http.UUIDSerializer
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class CreateProfile(
    private val profileRepository: ProfileRepository
): org.example.application.usecase.UseCase<CreateProfileRequest, CreateProfileResponse> {

    override fun execute(input: CreateProfileRequest): CreateProfileResponse {
        if (profileRepository.existsByUsername(input.username)) {
            throw IllegalArgumentException("Username already exists")
        }
        if (profileRepository.existsByEmail(input.email)) {
            throw IllegalArgumentException("Email already exists")
        }
        val profile = Profile.create(
            username = input.username,
            displayName = input.displayName,
            avatar = input.avatar,
            description = input.description,
            telephone = input.telephone,
            email = input.email,
            birthDate = input.birthDate,
            region = input.region
        )
        profileRepository.save(profile)
        return CreateProfileResponse(
            id = profile.id,
            username = profile.username,
            displayName = profile.displayName,
            avatar = profile.avatar,
            description = profile.description,
            status = profile.status,
            visibility = profile.visibility,
            telephone = profile.telephone,
            email = profile.email,
            birthDate = profile.birthDate,
            region = profile.region,
            createdAt = profile.createdAt,
            updatedAt = profile.updatedAt,
            deletedAt = profile.deletedAt
        )
    }
}

@Serializable
data class CreateProfileRequest(
    val username: String,
    val displayName: String,
    val avatar: String,
    val description: String,
    val telephone: String?,
    val email: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val region: String
)

@Serializable
data class CreateProfileResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String,
    val displayName: String,
    val avatar: String?,
    val description: String?,
    val status: ProfileStatus,
    val visibility: ProfileVisibility,
    val telephone: String?,
    val email: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val region: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant?
)