package org.example.application.port

import org.example.domain.Comment

interface CommentRepository {
    suspend fun save(comment: Comment)
}