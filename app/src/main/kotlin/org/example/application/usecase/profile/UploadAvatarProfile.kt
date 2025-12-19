package org.example.application.usecase.profile

import org.example.application.port.ProfileRepository
import org.example.application.usecase.UseCase
import java.io.File
import java.util.UUID

class UploadAvatarProfile(
    private val profileRepository: ProfileRepository
): UseCase<UploadAvatarProfile.Input, Unit> {

    private val allowedExtensions = listOf("jpg", "jpeg", "png")

    override fun execute(input: Input) {
        val profile = profileRepository.findById(input.profileId)
            ?: throw IllegalArgumentException("Profile not found")
        val fileExtension = input.fileName.substringAfterLast('.', "").lowercase()
        if (fileExtension !in allowedExtensions) {
            throw IllegalArgumentException("Invalid file type. Only JPG, JPEG, and PNG are allowed.")
        }
        val file = File("uploads/${input.profileId}/avatar.${fileExtension}")
        file.parentFile.mkdirs()
        file.writeBytes(input.fileBytes)
        profile.setAvatarPath(file.path)
        profileRepository.update(profile)
    }

    data class Input(
        val profileId: UUID,
        val fileName: String,
        val fileBytes: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Input

            if (profileId != other.profileId) return false
            if (fileName != other.fileName) return false
            if (!fileBytes.contentEquals(other.fileBytes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = profileId.hashCode()
            result = 31 * result + fileName.hashCode()
            result = 31 * result + fileBytes.contentHashCode()
            return result
        }
    }
}