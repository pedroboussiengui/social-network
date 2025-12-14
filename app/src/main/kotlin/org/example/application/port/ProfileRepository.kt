package org.example.application.port

import org.example.domain.Profile

interface ProfileRepository {

    fun save(profile: Profile)

    fun findByUsername(username: String): Profile?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}