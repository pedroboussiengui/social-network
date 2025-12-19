package org.example.application.usecase.profile

import kotlinx.serialization.Serializable
import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.PostStatus
import org.example.domain.PostType
import org.example.domain.PostVisibility
import org.example.infra.http.UUIDSerializer
import java.util.UUID

class ListProfilePosts(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository
): SuspendUseCase<ListProfilePosts.Input, List<ListProfilePosts.Output>> {

    override suspend fun execute(input: Input): List<Output> {
        // TODO: Check if the profile is visible to the current user
        val posts = postRepository.findByProfileId(input.profileId)
        return posts.map {
            Output(
                id = it.id,
                authorId = it.authorId,
                postType = it.postType,
                content = it.content,
                likeCount = likeRepository.countLikesByPostId(it.id),
                description = it.description,
                postVisibility = it.postVisibility,
                postStatus = it.postStatus
            )
        }
    }

    data class Input(
        val profileId: UUID,
        val principal: UUID
    )

    @Serializable
    data class Output(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        @Serializable(with = UUIDSerializer::class)
        val authorId: UUID,
        val postType: PostType,
        val content: String,
        val likeCount: Long,
        val description: String?,
        val postVisibility: PostVisibility,
        val postStatus: PostStatus,
    )
}