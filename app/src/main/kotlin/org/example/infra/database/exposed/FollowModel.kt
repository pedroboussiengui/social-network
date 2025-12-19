package org.example.infra.database.exposed

import org.jetbrains.exposed.v1.core.Table

object FollowModel : Table("follow") {
    val followerId = uuid("follower_id").index()
    val followedId = uuid("followed_id").index()

    override val primaryKey = PrimaryKey(followerId, followedId)
}