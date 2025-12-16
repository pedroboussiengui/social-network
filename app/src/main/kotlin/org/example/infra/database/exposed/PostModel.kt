package org.example.infra.database.exposed

import org.example.domain.PostStatus
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object PostModel : Table("post") {
    val id = uuid("id")
    val authorId = uuid("author_id")
    val postType = enumeration("post_type", PostType::class)
    val content = text("content")
    val description = text("description").nullable()
    val postVisibility = enumeration("post_visibility", PostVisibility::class)
    val postStatus = enumeration("post_status", PostStatus::class)
    val hashTags = text("hash_tags")
    val allowComments = bool("allow_comments")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}