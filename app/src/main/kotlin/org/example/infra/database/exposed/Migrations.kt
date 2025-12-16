package org.example.infra.database.exposed

import org.example.infra.database.exposed.PostModel
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object Migrations {

    fun run() {
        transaction {
            SchemaUtils.create(PostModel)
        }
    }
}