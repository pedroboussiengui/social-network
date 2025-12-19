package org.example.application.usecase.profile

import kotlinx.serialization.Serializable
import org.example.application.port.ProfileRepository
import org.example.application.usecase.UseCase
import org.example.domain.ProfileStatus
import org.example.domain.ProfileVisibility
import org.example.infra.http.UUIDSerializer
import java.util.*

class GetProfileByUsername(
    private val profileRepository: ProfileRepository
): UseCase<GetProfileByUsername.Input, GetProfileByUsername.Output> {

    override fun execute(input: Input): Output {
        val profile = profileRepository.findByUsername(input.username)
            ?: throw IllegalArgumentException("Profile not found")

        if (profile.deletedAt != null) {
            throw IllegalArgumentException("Profile has been deleted")
        }

        return Output(
            id = profile.id,
            username = profile.username,
            displayName = profile.displayName,
            avatar = profile.avatar,
            description = profile.description,
            status = profile.status,
            visibility = profile.visibility
        )
    }

    data class Input(val username: String)

    @Serializable
    data class Output(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val username: String,
        val displayName: String,
        val avatar: String?,
        val description: String?,
        val status: ProfileStatus,
        val visibility: ProfileVisibility
    )
}