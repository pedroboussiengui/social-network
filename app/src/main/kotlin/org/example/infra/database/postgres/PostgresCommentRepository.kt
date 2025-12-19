package org.example.infra.database.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.application.port.CommentRepository
import org.example.application.usecase.PageRequest
import org.example.application.usecase.PageResponse
import org.example.domain.Comment
import org.example.infra.database.exposed.CommentModel
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*
import kotlin.math.ceil

class PostgresCommentRepository : CommentRepository {

    override suspend fun save(comment: Comment) {
        withContext(Dispatchers.IO) {
            transaction {
                CommentModel.insert {
                    it[id] = comment.id
                    it[postId] = comment.postId
                    it[profileId] = comment.profileId
                    it[content] = comment.content
                    it[parentCommentId] = comment.parentCommentId
                    it[createdAt] = comment.createdAt
                }
            }
        }
    }

    override suspend fun findCommentsByPostId(pageRequest: PageRequest, postId: UUID): PageResponse<Comment> {
        return withContext(Dispatchers.IO) {
            transaction {
                val totalElements = CommentModel.selectAll().count()
                val items = CommentModel.selectAll()
                    .where { CommentModel.postId eq postId and CommentModel.parentCommentId.isNull() }
                    .orderBy(CommentModel.createdAt, SortOrder.DESC)
                    .limit(pageRequest.size)
                    .offset(pageRequest.offset)
                    .map {
                        Comment(
                            id = it[CommentModel.id],
                            postId = it[CommentModel.postId],
                            profileId = it[CommentModel.profileId],
                            content = it[CommentModel.content],
                            parentCommentId = it[CommentModel.parentCommentId],
                            createdAt = it[CommentModel.createdAt]
                        )
                    }
                PageResponse(
                    items = items,
                    page = pageRequest.page,
                    size = pageRequest.size,
                    totalItems = totalElements,
                    totalPages = ceil(totalElements / pageRequest.size.toDouble()).toInt()
                )
            }
        }
    }

    override suspend fun countCommentsByCommentId(commentId: UUID): Long {
        return withContext(Dispatchers.IO) {
            transaction {
                CommentModel.selectAll().where { CommentModel.parentCommentId eq commentId }.count()
            }
        }
    }
}