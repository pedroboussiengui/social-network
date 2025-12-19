package org.example.application.port

import org.example.domain.Follow
import java.util.UUID

interface FollowRepository {
    suspend fun follow(follow: Follow)
    suspend fun unfollow(follow: Follow)
    suspend fun isFollowing(follow: Follow): Boolean
    suspend fun countFollowers(followedId: UUID): Long
    suspend fun countFollowing(followerId: UUID): Long
    suspend fun getFollowers(followedId: UUID): List<UUID>
    suspend fun getFollowing(followerId: UUID): List<UUID>
}