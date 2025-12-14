package org.example.application.port

import org.example.domain.Post

interface PostRepository {

    suspend fun save(post: Post)

    suspend fun findById(id: String): Post?
}