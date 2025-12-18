package org.example.infra.database.postgres

import org.example.application.port.LikeRepository
import org.example.infra.database.exposed.LikeModel
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

class PostgresLikeRepository : LikeRepository {
    override suspend fun save(profileId: UUID, postId: UUID) {
        transaction {
            LikeModel.insert {
                it[LikeModel.profileId] = profileId
                it[LikeModel.postId] = postId
            }
        }
    }

    override suspend fun delete(profileId: UUID, postId: UUID) {
        transaction {
            LikeModel.deleteWhere {
                (LikeModel.profileId eq profileId) and (LikeModel.postId eq postId)
            }
        }
    }

    override suspend fun exists(profileId: UUID, postId: UUID): Boolean {
        return transaction {
            LikeModel.select((LikeModel.profileId eq profileId) and (LikeModel.postId eq postId)).any()
        }
    }

    override suspend fun countLikesByPostId(postId: UUID): Long {
        return transaction {
            LikeModel.select(LikeModel.postId eq postId).count()
        }
    }
}