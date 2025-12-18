package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class DeleteProfile(
    private val profileRepository: ProfileRepository
) : SuspendUseCase<UUID, Unit> {

    override suspend fun execute(input: UUID) {
        if (!profileRepository.existsById(input)) {
            throw IllegalArgumentException("Profile with ID $input does not exist.")
        }
        profileRepository.delete(input)
    }
}