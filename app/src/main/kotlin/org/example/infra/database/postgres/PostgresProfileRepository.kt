package org.example.infra.database.postgres

import org.example.application.port.ProfileRepository
import org.example.domain.Profile
import org.example.infra.database.exposed.ProfileModel
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant
import java.util.*

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
            ProfileModel.selectAll().where { ProfileModel.username eq username}.singleOrNull()?.let {
                toDomain(it)
            }
        }
    }

    override fun findById(id: UUID): Profile? {
        return transaction {
            ProfileModel.selectAll().where { ProfileModel.id eq id }.singleOrNull()?.let {
                toDomain(it)
            }
        }
    }

    override fun existsById(id: UUID): Boolean {
        return transaction {
            ProfileModel.selectAll().where { ProfileModel.id eq id }.any()
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

    override fun update(profile: Profile) {
        transaction {
            ProfileModel.update({ ProfileModel.id eq profile.id }) {
                it[displayName] = profile.displayName
                it[avatar] = profile.avatar
                it[description] = profile.description
                it[status] = profile.status
                it[visibility] = profile.visibility
                it[telephone] = profile.telephone
                it[email] = profile.email
                it[birthDate] = profile.birthDate
                it[region] = profile.region
                it[updatedAt] = Instant.now()
            }
        }
    }

    override fun searchByUsername(query: String): List<Profile> {
        return transaction {
            ProfileModel.selectAll()
                .where { ProfileModel.username like "%$query%" }
                .map { toDomain(it) }
        }
    }

    private fun toDomain(row: ResultRow): Profile = Profile(
        id = row[ProfileModel.id],
        username = row[ProfileModel.username],
        displayName = row[ProfileModel.displayName],
        avatar = row[ProfileModel.avatar],
        description = row[ProfileModel.description],
        status = row[ProfileModel.status],
        visibility = row[ProfileModel.visibility],
        telephone = row[ProfileModel.telephone],
        email = row[ProfileModel.email],
        birthDate = row[ProfileModel.birthDate],
        region = row[ProfileModel.region],
        createdAt = row[ProfileModel.createdAt],
        updatedAt = row[ProfileModel.updatedAt],
        deletedAt = row[ProfileModel.deletedAt]
    )
}