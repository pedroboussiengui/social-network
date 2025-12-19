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
): SuspendUseCase<CreatePostRequest, CreatePostResponse> {

    override suspend fun execute(input: CreatePostRequest): CreatePostResponse {
        val author = profileRepository.findById(input.authorId)
        if (author?.deletedAt != null) {
            throw IllegalArgumentException("Author not found")
        }
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
            likeCount = 0,
            allowComments = post.allowComments,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt
        )
    }
}

@Serializable
data class CreatePostRequest(
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
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