package org.example.infra.database.exposed

import org.example.domain.ProfileStatus
import org.example.domain.ProfileVisibility
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.timestamp

object ProfileModel: Table("profile") {
    val id = uuid("id")
    val username = varchar("username", 255).uniqueIndex()
    val displayName = varchar("display_name", 255)
    val avatar = varchar("avatar", 255).nullable()
    val description = text("description").nullable()
    val status = enumerationByName("status", 50, ProfileStatus::class)
    val visibility = enumerationByName("visibility", 50, ProfileVisibility::class)
    val telephone = varchar("telephone", 20).nullable()
    val email = varchar("email", 255).uniqueIndex()
    val birthDate = date("birth_date")
    val region = varchar("region", 255)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()
    val deletedAt = timestamp("deleted_at").nullable()

    override val primaryKey = PrimaryKey(id)
}