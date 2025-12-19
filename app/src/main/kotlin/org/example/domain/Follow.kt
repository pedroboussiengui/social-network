package org.example.domain

import java.util.UUID

class Follow(
    val followerId: UUID,
    val followedId: UUID
) {
}