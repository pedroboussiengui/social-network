package org.example.application.usecase

interface UseCase<K, V> {

    fun execute(input: K): V
}

interface SuspendUseCase<K, V> {

    suspend fun execute(input: K): V
}

interface SuspendUseCaseWithPagination<K, V> {

    suspend fun execute(pageRequest: PageRequest, input: K): V
}