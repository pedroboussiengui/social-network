package org.example.application.port

import java.util.UUID

interface LikeRepository {
    suspend fun save(profileId: UUID, postId: UUID)
    suspend fun delete(profileId: UUID, postId: UUID)
    suspend fun exists(profileId: UUID, postId: UUID): Boolean
    suspend fun countLikesByPostId(postId: UUID): Long
}