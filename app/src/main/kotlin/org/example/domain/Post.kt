package org.example.domain

import java.time.Instant
import java.util.UUID

class Post(
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
) {
    companion object {
        fun create(
            authorId: UUID,
            postType: PostType,
            content: String,
            description: String?,
            postVisibility: PostVisibility,
            hashTags: List<String>,
            allowComments: Boolean
        ): Post {
            return Post(
                id = UUID.randomUUID(),
                authorId = authorId,
                postType = postType,
                content = content,
                description = description,
                postVisibility = postVisibility,
                postStatus = PostStatus.ACTIVE,
                hashTags = hashTags,
                allowComments = allowComments,
                createdAt = Instant.now(),
                updatedAt = null
            )
        }
    }
}

enum class PostVisibility {
    PUBLIC, PRIVATE, ONLY_FRIENDS
}

enum class PostStatus {
    ACTIVE, EDITED, REMOVED
}

enum class PostType {
    TEXT, IMAGE, VIDEO, AUDIO
}
