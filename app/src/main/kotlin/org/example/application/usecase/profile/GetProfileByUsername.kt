package org.example.application.usecase.profile

import kotlinx.serialization.Serializable
import org.example.application.port.FollowRepository
import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.ProfileStatus
import org.example.domain.ProfileVisibility
import org.example.domain.exceptions.EntityNotFoundException
import org.example.infra.http.UUIDSerializer
import java.util.*

class GetProfileByUsername(
    private val profileRepository: ProfileRepository,
    private val followRepository: FollowRepository
): SuspendUseCase<GetProfileByUsername.Input, GetProfileByUsername.Output> {

    override suspend fun execute(input: Input): Output {
        val profile = profileRepository.findByUsername(input.username)
            ?: throw EntityNotFoundException("Profile with name ${input.username} does not exist")
        if (profile.deletedAt != null) {
            throw IllegalArgumentException("Profile has been deleted")
        }
        val countFollowers = followRepository.countFollowers(profile.id)
        val countFollowing = followRepository.countFollowing(profile.id)
        return Output(
            id = profile.id,
            username = profile.username,
            displayName = profile.displayName,
            avatar = profile.avatar,
            description = profile.description,
            countFollowers = countFollowers,
            countFollowing = countFollowing,
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
        val countFollowers: Long,
        val countFollowing: Long,
        val status: ProfileStatus,
        val visibility: ProfileVisibility
    )
}