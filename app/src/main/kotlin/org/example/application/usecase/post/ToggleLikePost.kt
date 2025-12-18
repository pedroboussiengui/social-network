package org.example.application.usecase.post

import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import org.example.application.port.ProfileRepository
import java.util.*

class ToggleLikePost(
    private val likeRepository: LikeRepository,
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository
): org.example.application.usecase.SuspendUseCase<LikePostRequest, Unit> {

    override suspend fun execute(input: LikePostRequest) {
//        if (!profileRepository.existsById(input.profileId)) {
//            throw IllegalArgumentException("Profile with ID ${input.profileId} does not exist.")
//        }
        if (!postRepository.existsById(input.postId)) {
            throw IllegalArgumentException("Post with ID ${input.postId} does not exist.")
        }
        if (likeRepository.exists(input.profileId, input.postId)) {
            likeRepository.delete(input.profileId, input.postId)
        } else {
            likeRepository.save(input.profileId, input.postId)
        }
    }
}

data class LikePostRequest(
    val profileId: UUID,
    val postId: UUID
)