package org.example.application.port

import org.example.domain.Post
import java.util.UUID

interface PostRepository {
    suspend fun save(post: Post)
    suspend fun findById(id: UUID): Post?
    suspend fun existsById(id: UUID): Boolean
    suspend fun deleteById(id: UUID)
}