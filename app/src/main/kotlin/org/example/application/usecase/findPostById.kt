package org.example.application.usecase

import org.example.application.port.PostRepository
import java.util.UUID

class FindPostById(
    private val postRepository: PostRepository
): SuspendUseCase<UUID, CreatePostResponse> {

    override suspend fun execute(input: UUID): CreatePostResponse {
        val post = postRepository.findById(input)
            ?: throw IllegalArgumentException("Post not found")
        return CreatePostResponse(
            id = post.id,
            authorId = post.authorId,
            postType = post.postType,
            content = post.content,
            description = post.description,
            postVisibility = post.postVisibility,
            postStatus = post.postStatus,
            hashTags = post.hashTags,
            allowComments = post.allowComments,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt
        )
    }
}