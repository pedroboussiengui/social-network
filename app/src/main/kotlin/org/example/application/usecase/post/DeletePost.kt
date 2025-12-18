package org.example.application.usecase.post

import org.example.application.port.PostRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class DeletePost(
    private val postRepository: PostRepository
): SuspendUseCase<UUID, Unit> {
    override suspend fun execute(input: UUID) {
        if (!postRepository.existsById(input)) {
            throw IllegalArgumentException("Post with ID $input does not exist")
        }
        postRepository.deleteById(input)
    }
}