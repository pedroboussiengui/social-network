package org.example.infra.database.postgres

import org.example.application.port.ProfileRepository
import org.example.domain.Profile
import org.example.infra.database.exposed.ProfileModel
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant
import java.util.UUID

class PostgresProfileRepository : ProfileRepository {
    override fun save(profile: Profile) {
        transaction {
            ProfileModel.insert {
                it[id] = profile.id
                it[username] = profile.username
                it[displayName] = profile.displayName
                it[avatar] = profile.avatar
                it[description] = profile.description
                it[status] = profile.status
                it[visibility] = profile.visibility
                it[telephone] = profile.telephone
                it[email] = profile.email
                it[birthDate] = profile.birthDate
                it[region] = profile.region
                it[createdAt] = profile.createdAt
                it[updatedAt] = profile.updatedAt
                it[deletedAt] = profile.deletedAt
            }
        }
    }

    override fun findByUsername(username: String): Profile? {
        return transaction {
            ProfileModel.select(ProfileModel.username eq username).singleOrNull()?.let {
                Profile(
                    id = it[ProfileModel.id],
                    username = it[ProfileModel.username],
                    displayName = it[ProfileModel.displayName],
                    avatar = it[ProfileModel.avatar],
                    description = it[ProfileModel.description],
                    status = it[ProfileModel.status],
                    visibility = it[ProfileModel.visibility],
                    telephone = it[ProfileModel.telephone],
                    email = it[ProfileModel.email],
                    birthDate = it[ProfileModel.birthDate],
                    region = it[ProfileModel.region],
                    createdAt = it[ProfileModel.createdAt],
                    updatedAt = it[ProfileModel.updatedAt],
                    deletedAt = it[ProfileModel.deletedAt]
                )
            }
        }
    }

    override fun existsById(id: UUID): Boolean {
        return transaction {
            ProfileModel.select(ProfileModel.id eq id).any()
        }
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

    override fun delete(id: UUID) {
        transaction {
            ProfileModel.update({ ProfileModel.id eq id }) {
                it[deletedAt] = Instant.now()
            }
        }
    }
}