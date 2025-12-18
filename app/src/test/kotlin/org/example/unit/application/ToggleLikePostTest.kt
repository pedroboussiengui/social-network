package org.example.unit.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.example.application.port.LikeRepository
import org.example.application.usecase.post.LikePostRequest
import org.example.application.usecase.post.ToggleLikePost
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class ToggleLikePostTest {

    @MockK
    private lateinit var likeRepository: LikeRepository

    @InjectMockKs
    private lateinit var toggleLikePost: ToggleLikePost

    @Test
    fun `should save like when it does not exist`() = runTest {
        // Given
        val profileId = UUID.randomUUID()
        val postId = UUID.randomUUID()
        val request = LikePostRequest(profileId, postId)

        coEvery { likeRepository.exists(profileId, postId) } returns false
        coEvery { likeRepository.save(profileId, postId) } just runs

        // When
        toggleLikePost.execute(request)

        // Then
        coVerify(exactly = 1) { likeRepository.exists(profileId, postId) }
        coVerify(exactly = 1) { likeRepository.save(profileId, postId) }
        coVerify(exactly = 0) { likeRepository.delete(any(), any()) }
    }

    @Test
    fun `should delete like when it already exists`() = runTest {
        // Given
        val profileId = UUID.randomUUID()
        val postId = UUID.randomUUID()
        val request = LikePostRequest(profileId, postId)

        coEvery { likeRepository.exists(profileId, postId) } returns true
        coEvery { likeRepository.delete(profileId, postId) } just runs

        // When
        toggleLikePost.execute(request)

        // Then
        coVerify(exactly = 1) { likeRepository.exists(profileId, postId) }
        coVerify(exactly = 1) { likeRepository.delete(profileId, postId) }
        coVerify(exactly = 0) { likeRepository.save(any(), any()) }
    }
}