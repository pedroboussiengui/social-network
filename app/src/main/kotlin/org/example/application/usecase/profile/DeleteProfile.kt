package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class DeleteProfile(
    private val profileRepository: ProfileRepository
) : SuspendUseCase<DeleteProfile.Input, Unit> {

    override suspend fun execute(input: Input) {
        if (!profileRepository.existsById(input.profileId)) {
            throw IllegalArgumentException("Profile with ID $input does not exist.")
        }
        val profile = profileRepository.findById(input.profileId)!!
        if (profile.id != input.principal) {
            throw IllegalArgumentException("You are not authorized to delete this profile.")
        }
        profileRepository.delete(input.profileId)
    }

    data class Input(
        val principal: UUID,
        val profileId: UUID
    )
}