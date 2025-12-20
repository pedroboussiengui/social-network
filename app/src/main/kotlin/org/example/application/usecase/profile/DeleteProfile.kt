package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.exceptions.EntityNotFoundException
import org.example.domain.exceptions.ProfileAccessDeniedException
import java.util.UUID

class DeleteProfile(
    private val profileRepository: ProfileRepository
) : SuspendUseCase<DeleteProfile.Input, Unit> {

    override suspend fun execute(input: Input) {
        val profile = profileRepository.findById(input.profileId)
            ?: throw EntityNotFoundException("Profile with ID $input does not exist")
        if (profile.id != input.principal) {
            throw ProfileAccessDeniedException("You are not authorized to delete this profile.")
        }
        profileRepository.delete(input.profileId)
    }

    data class Input(
        val principal: UUID,
        val profileId: UUID
    )
}