package org.example.infra.database.postgres

import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseConnection {
    fun init(config: ApplicationConfig) {
        Database.Companion.connect(
            url = config.property("database.jdbcUrl").getString(),
            driver = "org.postgresql.Driver",
            user = config.property("database.username").getString(),
            password = config.property("database.password").getString()
        )
    }
}