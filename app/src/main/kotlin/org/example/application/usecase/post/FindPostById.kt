package org.example.application.usecase.post

import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import org.example.application.usecase.SuspendUseCase
import java.util.UUID

class FindPostById(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository
): SuspendUseCase<FindPostById.Input, CreatePostResponse> {

    override suspend fun execute(input: Input): CreatePostResponse {
        val post = postRepository.findById(input.postId)
            ?: throw IllegalArgumentException("Post with ID $input does not exist")
        if (post.isPrivate() && post.authorId != input.principal) {
            throw IllegalArgumentException("This post is available only to the author")
        }
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

    data class Input(
        val principal: UUID,
        val postId: UUID
    )
}