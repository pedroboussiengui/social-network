package org.example.infra.database.exposed

import org.jetbrains.exposed.v1.core.Table

object LikeModel : Table("like") {
    val profileId = uuid("profile_id").index()
    val postId = uuid("post_id").index()

    override val primaryKey = PrimaryKey(profileId, postId)

    init {
        index(false,profileId, postId)
    }
}