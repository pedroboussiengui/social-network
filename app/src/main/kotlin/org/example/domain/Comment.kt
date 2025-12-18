package org.example.domain

import java.time.Instant
import java.util.UUID

class Comment(
    val id: UUID,
    val postId: UUID,
    val profileId: UUID,
    val content: String,
    val parentCommentId: UUID?,
    val createdAt: Instant
) {
    companion object {
        fun create(
            postId: UUID,
            profileId: UUID,
            content: String,
            parentCommentId: UUID?
        ): Comment {
            return Comment(
                id = UUID.randomUUID(),
                postId = postId,
                profileId = profileId,
                content = content,
                parentCommentId = parentCommentId,
                createdAt = Instant.now()
            )
        }

        fun restore(
            id: UUID,
            postId: UUID,
            profileId: UUID,
            content: String,
            parentCommentId: UUID?,
            createdAt: Instant
        ): Comment {
            return Comment(
                id = id,
                postId = postId,
                profileId = profileId,
                content = content,
                parentCommentId = parentCommentId,
                createdAt = createdAt
            )
        }
    }
}