package org.example.application.usecase.comment

import kotlinx.serialization.Serializable
import org.example.application.port.CommentRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.Comment
import org.example.infra.http.InstantSerializer
import org.example.infra.http.UUIDSerializer
import java.time.Instant
import java.util.UUID

class CommentPost(
    private val commentRepository: CommentRepository
): SuspendUseCase<CommentPost.Input, CommentPostResponse> {
    override suspend fun execute(input: Input): CommentPostResponse {
        val comment = Comment.create(
            profileId = input.profileId,
            postId = input.postId,
            content = input.request.content,
            parentCommentId = input.request.parentCommentId
        )
        commentRepository.save(comment)
        return CommentPostResponse(
            id = comment.id,
            profileId = comment.profileId,
            postId = comment.postId,
            content = comment.content,
            responses = 0,
            parentCommentId = comment.parentCommentId,
            createdAt = comment.createdAt
        )
    }

    data class Input(
        val postId: UUID,
        val profileId: UUID,
        val request: CommentPostRequest
    )
}

@Serializable
data class CommentPostRequest(
    val content: String,
    @Serializable(with = UUIDSerializer::class)
    val parentCommentId: UUID?,
)

@Serializable
data class CommentPostResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val profileId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val postId: UUID,
    val content: String,
    val responses: Long,
    @Serializable(with = UUIDSerializer::class)
    val parentCommentId: UUID?,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant
)