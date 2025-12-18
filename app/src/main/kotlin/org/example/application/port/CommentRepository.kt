package org.example.application.port

import org.example.application.usecase.PageRequest
import org.example.application.usecase.PageResponse
import org.example.domain.Comment
import java.util.*

interface CommentRepository {
    suspend fun save(comment: Comment)
    suspend fun findCommentsByPostId(pageRequest: PageRequest, postId: UUID): PageResponse<Comment>
}