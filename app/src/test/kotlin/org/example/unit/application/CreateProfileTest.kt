package org.example.unit.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.application.port.ProfileRepository
import org.example.application.usecase.CreateProfile
import org.example.application.usecase.CreateProfileRequest
import org.example.domain.ProfileStatus
import org.example.domain.ProfileVisibility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test

class CreateProfileTest {

    @Test
    fun `crateProfile should create a new profile successfully`() {
        // arrange
        val mockProfileRepository = mockk<ProfileRepository>()
        val createProfile = CreateProfile(mockProfileRepository)

        // stubbing the behavior
        every { mockProfileRepository.existsByUsername(any()) } returns false
        every { mockProfileRepository.existsByEmail(any()) } returns false
        every { mockProfileRepository.save(any()) } answers { nothing }

        // act
        val input = CreateProfileRequest(
            username = "@john.doe",
            displayName = "John Doe",
            avatar = "https://example.com/avatar.jpg",
            description = "A short description",
            telephone = "+55-11-99999-9999",
            email = "john.doe@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            region = "BR"
        )
        val output = createProfile.execute(input)

        // assert
        assertNotNull(output.id)
        assertInstanceOf<UUID>(output.id)
        assertEquals("@john.doe", output.username)
        assertEquals("John Doe", output.displayName)
        assertEquals("https://example.com/avatar.jpg", output.avatar)
        assertEquals("A short description", output.description)
        assertEquals(ProfileStatus.ACTIVE, output.status)
        assertEquals(ProfileVisibility.PUBLIC, output.visibility)
        assertNotNull(output.telephone)
        assertEquals("john.doe@example.com", output.email)
        assertEquals(LocalDate.of(1990, 1, 1), output.birthDate)
        assertEquals("BR", output.region)
        assertNotNull(output.createdAt)
        assertNull(output.updatedAt)

        verify(exactly = 1) { mockProfileRepository.existsByUsername(input.username) }
        verify(exactly = 1) { mockProfileRepository.existsByEmail(input.email) }
        verify(exactly = 1) { mockProfileRepository.save(any()) }
    }

    @Test
    fun `createProfile should fail when username already exists`() {
        // arrange
        val mockProfileRepository = mockk<ProfileRepository>()
        val createProfile = CreateProfile(mockProfileRepository)

        // stubbing the behavior
        every { mockProfileRepository.existsByUsername(any()) } returns true

        // act
        val input = CreateProfileRequest(
            username = "@john.doe",
            displayName = "John Doe",
            avatar = "https://example.com/avatar.jpg",
            description = "A short description",
            telephone = "+55-11-99999-9999",
            email = "john.doe@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            region = "BR"
        )
        val exception = assertThrows<IllegalArgumentException> {
            createProfile.execute(input)
        }

        // assert
        assertEquals("Username already exists", exception.message)

        verify(exactly = 1) { mockProfileRepository.existsByUsername(input.username) }
        verify(exactly = 0) { mockProfileRepository.existsByEmail(any()) }
        verify(exactly = 0) { mockProfileRepository.save(any()) }
    }
}