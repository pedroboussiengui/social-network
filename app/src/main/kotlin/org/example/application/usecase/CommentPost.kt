package org.example.application.usecase

import kotlinx.serialization.Serializable
import org.example.application.port.CommentRepository
import org.example.domain.Comment
import org.example.infra.http.InstantSerializer
import org.example.infra.http.UUIDSerializer
import java.time.Instant
import java.util.UUID

class CommentPost(
    private val commentRepository: CommentRepository
): UseCase<CommentPostRequest, CommentPostResponse> {
    override fun execute(input: CommentPostRequest): CommentPostResponse {
        val comment = Comment.create(
            authorId = input.authorId,
            postId = input.postId!!,
            content = input.content,
            parentCommentId = input.parentCommentId
        )
        // TODO: Save the comment using commentPostRepository
        return CommentPostResponse(
            id = comment.id,
            authorId = comment.authorId,
            postId = comment.postId,
            content = comment.content,
            parentCommentId = comment.parentCommentId,
            createdAt = comment.createdAt
        )
    }
}

@Serializable
data class CommentPostRequest(
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    val content: String,
    @Serializable(with = UUIDSerializer::class)
    val parentCommentId: UUID?,
) {
    @Serializable(with = UUIDSerializer::class)
    var postId: UUID? = null
        private set

    fun setPostId(postId: UUID) {
        this.postId = postId
    }
}

@Serializable
data class CommentPostResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val postId: UUID,
    val content: String,
    @Serializable(with = UUIDSerializer::class)
    val parentCommentId: UUID?,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant
)