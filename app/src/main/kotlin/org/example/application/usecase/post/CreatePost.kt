package org.example.application.usecase.post

import kotlinx.serialization.Serializable
import org.example.application.port.PostRepository
import org.example.application.port.ProfileRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.Post
import org.example.domain.PostStatus
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.example.infra.http.InstantSerializer
import org.example.infra.http.UUIDSerializer
import java.time.Instant
import java.util.UUID

class CreatePost(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository
): SuspendUseCase<CreatePost.Input, CreatePostResponse> {

    override suspend fun execute(input: Input): CreatePostResponse {
        val author = profileRepository.findById(input.principal)
            ?: throw IllegalArgumentException("Author not found")
        if (author.isDeleted()) {
            throw IllegalArgumentException("Deleted profile cannot create posts")
        }
        val post = Post.create(
            authorId = input.principal,
            postType = input.request.postType,
            content = input.request.content,
            description = input.request.description,
            postVisibility = input.request.postVisibility,
            hashTags = input.request.hashTags,
            allowComments = input.request.allowComments
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
            likeCount = 0,
            allowComments = post.allowComments,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt
        )
    }

    data class Input(
        val principal: UUID,
        val request: CreatePostRequest
    )
}

@Serializable
data class CreatePostRequest(
    val postType: PostType,
    val content: String,
    val description: String?,
    val postVisibility: PostVisibility,
    val hashTags: List<String>,
    val allowComments: Boolean
)

@Serializable
data class CreatePostResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    val postType: PostType,
    val content: String,
    val description: String?,
    val postVisibility: PostVisibility,
    val postStatus: PostStatus,
    val hashTags: List<String>,
    val likeCount: Long,
    val allowComments: Boolean,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?
)