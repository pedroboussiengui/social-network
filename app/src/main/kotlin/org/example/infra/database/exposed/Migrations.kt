package org.example.infra.database.exposed

import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object Migrations {

    fun createAll() {
        transaction {
            SchemaUtils.create(PostModel, ProfileModel, CommentModel, LikeModel)
        }
    }

    fun dropAll() {
        transaction {
            SchemaUtils.drop(PostModel, ProfileModel, CommentModel, LikeModel)
        }
    }
}