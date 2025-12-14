package org.example.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.time.LocalDate
import kotlin.test.Test

class ProfileTest {

    @Test
    fun `Profile can be created`() {
        val profile = Profile.create(
            username = "@john.doe",
            displayName = "John Doe",
            avatar = "https://example.com/avatar.jpg",
            telephone = null,
            description = "A short description",
            email = "john.doe@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            region = "US"
        )

        assertNotNull(profile.id)
        assertEquals("@john.doe", profile.username)
        assertEquals("John Doe", profile.displayName)
        assertEquals("https://example.com/avatar.jpg", profile.avatar)
        assertEquals("A short description", profile.description)
        assertEquals(ProfileStatus.ACTIVE, profile.status)
        assertEquals(ProfileVisibility.PUBLIC, profile.visibility)
        assertNull(profile.telephone)
        assertEquals("john.doe@example.com", profile.email)
        assertEquals(LocalDate.of(1990, 1, 1), profile.birthDate)
        assertEquals("US", profile.region)
        assertNotNull(profile.createdAt)
        assertNull(profile.updatedAt)
    }

    @Test
    fun `Profile can be created with telephone`() {
        val profile = Profile.create(
            username = "@john.doe",
            displayName = "John Doe",
            avatar = "https://example.com/avatar.jpg",
            telephone = "+1-555-123-4567",
            description = "A short description",
            email = "john.doe@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            region = "US"
        )

        assertNotNull(profile.id)
        assertEquals("@john.doe", profile.username)
        assertEquals("John Doe", profile.displayName)
        assertEquals("https://example.com/avatar.jpg", profile.avatar)
        assertEquals("A short description", profile.description)
        assertEquals(ProfileStatus.ACTIVE, profile.status)
        assertEquals(ProfileVisibility.PUBLIC, profile.visibility)
        assertEquals("+1-555-123-4567", profile.telephone)
        assertEquals("john.doe@example.com", profile.email)
        assertEquals(LocalDate.of(1990, 1, 1), profile.birthDate)
        assertEquals("US", profile.region)
        assertNotNull(profile.createdAt)
        assertNull(profile.updatedAt)
    }

}