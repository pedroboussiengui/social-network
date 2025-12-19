package org.example.application.usecase.post

import org.example.application.port.PostRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class DeletePost(
    private val postRepository: PostRepository
): SuspendUseCase<DeletePost.Input, Unit> {
    override suspend fun execute(input: DeletePost.Input) {
        if (!postRepository.existsById(input.postId)) {
            throw IllegalArgumentException("Post with ID ${input.postId} does not exist")
        }
        val post = postRepository.findById(input.postId)
        if (post!!.authorId != input.profileId) {
            throw IllegalArgumentException("Profile with ID ${input.profileId} is not the author of the post with ID ${input.postId}")
        }
        postRepository.deleteById(input.postId)
    }

    data class Input(
        val postId: UUID,
        val profileId: UUID
    )
}