package org.example.application.usecase.profile

import org.example.application.port.FollowRepository
import org.example.application.usecase.SuspendUseCase
import org.example.domain.Follow
import java.util.UUID

class ToggleFollowProfile(
    private val followRepository: FollowRepository
): SuspendUseCase<ToggleFollowProfile.Input, Unit> {

    override suspend fun execute(input: Input) {
        if (input.followerId == input.followedId) {
            throw IllegalArgumentException("Cannot follow yourself")
        }
        val follow = Follow(
            followerId = input.followerId,
            followedId = input.followedId
        )
        if (followRepository.isFollowing(follow)) {
            followRepository.unfollow(follow)
        } else {
            followRepository.follow(follow)
        }
    }

    data class Input(
        val followerId: UUID,
        val followedId: UUID
    )
}