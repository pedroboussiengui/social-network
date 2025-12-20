package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.exceptions.EntityNotFoundException
import org.example.domain.exceptions.ProfileAccessDeniedException
import java.util.UUID

class PrivateProfile(
    private val profileRepository: ProfileRepository
) : SuspendUseCase<PrivateProfile.Input, Unit> {

    override suspend fun execute(input: Input) {
        val profile = profileRepository.findById(input.profileId)
            ?: throw EntityNotFoundException("Profile with ID ${input.profileId} does not exist")
        if (profile.id != input.principal) {
            throw ProfileAccessDeniedException("You are not authorized to make this profile private")
        }
        profile.toPrivate()
        profileRepository.update(profile)
    }

    data class Input(
        val principal: UUID,
        val profileId: UUID
    )
}