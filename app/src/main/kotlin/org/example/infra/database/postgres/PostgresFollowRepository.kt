package org.example.infra.database.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.application.port.FollowRepository
import org.example.domain.Follow
import org.example.infra.database.exposed.FollowModel
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

class PostgresFollowRepository : FollowRepository {
    
    override suspend fun follow(follow: Follow) {
        withContext(Dispatchers.IO) {
            transaction {
                FollowModel.insert {
                    it[followerId] = follow.followerId
                    it[followedId] = follow.followedId
                }
            }
        }
    }

    override suspend fun unfollow(follow: Follow) {
        withContext(Dispatchers.IO) {
            transaction {
                FollowModel.deleteWhere {
                    (FollowModel.followerId eq follow.followerId) and (FollowModel.followedId eq follow.followedId)
                }
            }
        }
    }

    override suspend fun isFollowing(follow: Follow): Boolean {
        return withContext(Dispatchers.IO) {
            transaction {
                FollowModel.selectAll().where {
                    (FollowModel.followerId eq follow.followerId) and (FollowModel.followedId eq follow.followedId)
                }.any()
            }
        }
    }

    override suspend fun countFollowers(followedId: UUID): Long {
        return withContext(Dispatchers.IO) {
            transaction {
                FollowModel.selectAll().where { FollowModel.followedId eq followedId }.count()
            }
        }
    }

    override suspend fun countFollowing(followerId: UUID): Long {
        return withContext(Dispatchers.IO) {
            transaction {
                FollowModel.selectAll().where { FollowModel.followerId eq followerId }.count()
            }
        }
    }

    override suspend fun getFollowers(followedId: UUID): List<UUID> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowing(followerId: UUID): List<UUID> {
        TODO("Not yet implemented")
    }
}