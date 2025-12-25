package org.example.application.usecase.profile

import org.example.application.usecase.UseCase
import org.example.domain.exceptions.EntityNotFoundException
import java.io.File
import java.util.*

class DownloadAvatarProfile: UseCase<DownloadAvatarProfile.Input, DownloadAvatarProfile.Output> {

    override fun execute(input: Input): Output {
        val file = File("uploads/${input.profileId}/${input.fileName}")
        if (!file.exists()) {
            throw EntityNotFoundException("Avatar file not found")
        }
        return Output(fileBytes = file.readBytes(), fileName = file.name)
    }

    data class Input(
        val profileId: UUID,
        val fileName: String
    )

    data class Output(
        val fileBytes: ByteArray,
        val fileName: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Output

            if (!fileBytes.contentEquals(other.fileBytes)) return false
            if (fileName != other.fileName) return false

            return true
        }

        override fun hashCode(): Int {
            var result = fileBytes.contentHashCode()
            result = 31 * result + fileName.hashCode()
            return result
        }
    }
}