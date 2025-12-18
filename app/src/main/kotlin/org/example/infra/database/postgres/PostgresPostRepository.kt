package org.example.infra.database.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.application.port.PostRepository
import org.example.domain.Post
import org.example.infra.database.exposed.PostModel
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

class PostgresPostRepository : PostRepository {
    override suspend fun save(post: Post) {
        withContext(Dispatchers.IO) {
            transaction {
                PostModel.insert {
                    it[id] = post.id
                    it[authorId] = post.authorId
                    it[postType] = post.postType
                    it[content] = post.content
                    it[description] = post.description
                    it[postVisibility] = post.postVisibility
                    it[postStatus] = post.postStatus
                    it[hashTags] = post.hashTags.joinToString(",")
                    it[allowComments] = post.allowComments
                    it[createdAt] = post.createdAt
                    it[updatedAt] = post.updatedAt
                }
            }
        }
    }

    override suspend fun findById(id: UUID): Post? {
        return withContext(Dispatchers.IO) {
            transaction {
                PostModel.selectAll().where { PostModel.id eq id }.singleOrNull()?.let {
                    Post(
                        id = it[PostModel.id],
                        authorId = it[PostModel.authorId],
                        postType = it[PostModel.postType],
                        content = it[PostModel.content],
                        description = it[PostModel.description],
                        postVisibility = it[PostModel.postVisibility],
                        postStatus = it[PostModel.postStatus],
                        hashTags = it[PostModel.hashTags].split(","),
                        allowComments = it[PostModel.allowComments],
                        createdAt = it[PostModel.createdAt],
                        updatedAt = it[PostModel.updatedAt]
                    )
                }
            }
        }
    }

    override suspend fun existsById(id: UUID): Boolean {
        return withContext(Dispatchers.IO) {
            transaction {
                PostModel.selectAll().where { PostModel.id eq id }.any()
            }
        }
    }

    override suspend fun deleteById(id: UUID) {
        withContext(Dispatchers.IO) {
            transaction {
                PostModel.deleteWhere { PostModel.id eq id }
            }
        }
    }
}