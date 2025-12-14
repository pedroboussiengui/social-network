package org.example.unit.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.application.port.PostRepository
import org.example.application.usecase.CreatePost
import org.example.application.usecase.CreatePostRequest
import org.example.domain.PostStatus
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.test.Test

class CreatePostTest {

    @Test
    fun `createPost should create a post successfully`() = runTest {
        // arrange
        val mockPostRepository = mockk<PostRepository>()
        val createPost = CreatePost(mockPostRepository)

        coEvery{ mockPostRepository.save(any()) } answers { nothing }

        val authorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")

        // act
        val input = CreatePostRequest(
            authorId = authorId,
            postType = PostType.TEXT,
            content = "This is a test post content.",
            description = "This is a test post description.",
            postVisibility = PostVisibility.PUBLIC,
            hashTags = listOf("#test", "#kotlin"),
            allowComments = true
        )
        val output = createPost.execute(input)

        // assert
        assertNotNull(output.id)
        assertEquals(authorId, output.authorId)
        assertEquals(PostType.TEXT, output.postType)
        assertEquals("This is a test post content.", output.content)
        assertEquals("This is a test post description.", output.description)
        assertEquals(PostVisibility.PUBLIC, output.postVisibility)
        assertEquals(PostStatus.ACTIVE, output.postStatus)
        assertEquals(listOf("#test", "#kotlin"), output.hashTags)
        assertEquals(true, output.allowComments)
        assertNotNull(output.createdAt)
        assertNull(output.updatedAt)

        coVerify(exactly = 1) { mockPostRepository.save(any()) }
    }
}