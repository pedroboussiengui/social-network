package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class PrivateProfile(
    private val profileRepository: ProfileRepository
) : SuspendUseCase<PrivateProfile.Input, Unit> {

    override suspend fun execute(input: Input) {
        val profile = profileRepository.findById(input.profileId)
            ?: throw IllegalArgumentException("Profile with ID ${input.profileId} does not exist")
        if (profile.id != input.principal) {
            throw IllegalArgumentException("Principal with ID ${input.principal} is not the owner of the profile with ID ${input.profileId}")
        }
        profile.toPrivate()
        profileRepository.update(profile)
    }

    data class Input(
        val principal: UUID,
        val profileId: UUID
    )
}