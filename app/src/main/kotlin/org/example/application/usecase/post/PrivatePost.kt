package org.example.application.usecase.post

import org.example.application.port.PostRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class PrivatePost(
    private val postRepository: PostRepository
) : SuspendUseCase<PrivatePostRequest, Unit> {
    override suspend fun execute(input: PrivatePostRequest) {
        val post = postRepository.findById(input.postId)
            ?: throw IllegalArgumentException("Post with ID ${input.postId} does not exist")
        // apenas o post owner pode privar o post
        if (post.authorId != input.profileId) {
            throw IllegalArgumentException("Profile with ID ${input.profileId} is not the author of the post with ID ${input.postId}")
        }
        post.privatePost()
        postRepository.update(post)
    }
}

data class PrivatePostRequest(
    val postId: UUID,
    val profileId: UUID
)