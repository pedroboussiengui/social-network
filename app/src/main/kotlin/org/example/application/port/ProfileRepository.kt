package org.example.application.port

import org.example.domain.Profile
import java.util.UUID

interface ProfileRepository {
    fun save(profile: Profile)
    fun findByUsername(username: String): Profile?
    fun findById(id: UUID): Profile?
    fun existsById(id: UUID): Boolean
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun delete(id: UUID)
    fun update(profile: Profile)
    fun searchByUsername(query: String): List<Profile>
}