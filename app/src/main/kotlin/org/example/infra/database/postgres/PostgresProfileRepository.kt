package org.example.infra.database.postgres

import org.example.application.port.ProfileRepository
import org.example.domain.Profile
import org.example.infra.database.exposed.ProfileModel
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PostgresProfileRepository : ProfileRepository {
    override fun save(profile: Profile) {
        TODO("Not yet implemented")
    }

    override fun findByUsername(username: String): Profile? {
        TODO("Not yet implemented")
    }

    override fun existsByUsername(username: String): Boolean {
        return transaction {
            ProfileModel.selectAll().where { ProfileModel.username eq username }.any()
        }
    }

    override fun existsByEmail(email: String): Boolean {
        return transaction {
            ProfileModel.selectAll().where { ProfileModel.email eq email }.any()
        }
    }

}