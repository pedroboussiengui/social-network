package org.example.application.usecase

import org.example.application.port.LikeRepository
import java.util.UUID

class ToggleLikePost(
    private val likeRepository: LikeRepository
): SuspendUseCase<LikePostRequest, Unit> {

    override suspend fun execute(input: LikePostRequest) {
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