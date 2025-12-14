package org.example.application.usecase

import org.example.application.port.PostRepository
import org.example.domain.Post
import org.example.domain.PostStatus
import org.example.domain.PostType
import org.example.domain.PostVisibility
import java.time.Instant
import java.util.UUID

class CreatePost(
    private val postRepository: PostRepository
): SuspendUseCase<CreatePostRequest, CreatePostResponse> {

    override suspend fun execute(input: CreatePostRequest): CreatePostResponse {
        val post = Post.create(
            authorId = input.authorId,
            postType = input.postType,
            content = input.content,
            description = input.description,
            postVisibility = input.postVisibility,
            hashTags = input.hashTags,
            allowComments = input.allowComments
        )
        postRepository.save(post)
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

data class CreatePostRequest(
    val authorId: UUID,
    val postType: PostType,
    val content: String,
    val description: String?,
    val postVisibility: PostVisibility,
    val hashTags: List<String>,
    val allowComments: Boolean
)

data class CreatePostResponse(
    val id: UUID,
    val authorId: UUID,
    val postType: PostType,
    val content: String,
    val description: String?,
    val postVisibility: PostVisibility,
    val postStatus: PostStatus,
    val hashTags: List<String>,
    val allowComments: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant?
)