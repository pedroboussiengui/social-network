package org.example.application.usecase

data class PageRequest(
    val page: Int,
    val size: Int
) {
    val offset: Long
        get() = ((page - 1) * size).toLong()
}