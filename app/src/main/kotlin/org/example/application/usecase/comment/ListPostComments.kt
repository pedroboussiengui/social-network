package org.example.application.usecase.comment

import org.example.application.port.CommentRepository
import org.example.application.usecase.PageRequest
import org.example.application.usecase.PageResponse
import org.example.application.usecase.SuspendUseCaseWithPagination
import java.util.*

class ListPostComments(
    private val commentRepository: CommentRepository
): SuspendUseCaseWithPagination<UUID, PageResponse<CommentPostResponse>> {

    override suspend fun execute(pageRequest: PageRequest, postId: UUID): PageResponse<CommentPostResponse> {
        val commentsPaginated = commentRepository.findCommentsByPostId(pageRequest, postId)
        return PageResponse(
            items = commentsPaginated.items.map {
                CommentPostResponse(
                    id = it.id,
                    profileId = it.profileId,
                    postId = it.postId,
                    content = it.content,
                    responses = commentRepository.countCommentsByCommentId(it.id),
                    parentCommentId = it.parentCommentId,
                    createdAt = it.createdAt
                )
            },
            page = commentsPaginated.page,
            size = commentsPaginated.size,
            totalItems = commentsPaginated.totalItems,
            totalPages = commentsPaginated.totalPages
        )
    }
}