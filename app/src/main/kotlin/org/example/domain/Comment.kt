package org.example.domain

import java.time.Instant
import java.util.UUID

class Comment(
    val id: UUID,
    val postId: UUID,
    val authorId: UUID,
    val content: String,
    val parentCommentId: UUID?,
    val createdAt: Instant
) {
    companion object {
        fun create(
            postId: UUID,
            authorId: UUID,
            content: String,
            parentCommentId: UUID?
        ): Comment {
            return Comment(
                id = UUID.randomUUID(),
                postId = postId,
                authorId = authorId,
                content = content,
                parentCommentId = parentCommentId,
                createdAt = Instant.now()
            )
        }

        fun restore(
            id: UUID,
            postId: UUID,
            authorId: UUID,
            content: String,
            parentCommentId: UUID?,
            createdAt: Instant
        ): Comment {
            return Comment(
                id = id,
                postId = postId,
                authorId = authorId,
                content = content,
                parentCommentId = parentCommentId,
                createdAt = createdAt
            )
        }
    }
}