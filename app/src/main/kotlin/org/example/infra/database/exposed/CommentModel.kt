package org.example.infra.database.exposed

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object CommentModel : Table("comment") {
    val id = uuid("id")
    val postId = uuid("post_id").references(PostModel.id)
    val profileId = uuid("profile_id").references(ProfileModel.id)
    val content = text("content")
    val parentCommentId = uuid("parent_comment_id").references(id).nullable()
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}
