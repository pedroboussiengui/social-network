package org.example.application.usecase

import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import java.util.UUID

class FindPostById(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository
): SuspendUseCase<UUID, CreatePostResponse> {

    override suspend fun execute(input: UUID): CreatePostResponse {
        val post = postRepository.findById(input)
            ?: throw IllegalArgumentException("Post not found")
        val likeCount = likeRepository.countLikesByPostId(post.id)
        return CreatePostResponse(
            id = post.id,
            authorId = post.authorId,
            postType = post.postType,
            content = post.content,
            description = post.description,
            postVisibility = post.postVisibility,
            postStatus = post.postStatus,
            hashTags = post.hashTags,
            likeCount = likeCount,
            allowComments = post.allowComments,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt
        )
    }
}