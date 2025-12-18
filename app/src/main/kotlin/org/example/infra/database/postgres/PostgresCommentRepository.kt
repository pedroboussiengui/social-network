package org.example.infra.database.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.application.port.CommentRepository
import org.example.domain.Comment
import org.example.infra.database.exposed.CommentModel
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

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
}