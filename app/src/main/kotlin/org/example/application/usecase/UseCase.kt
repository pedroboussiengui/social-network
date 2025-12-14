package org.example.application.usecase

interface UseCase<K, V> {

    fun execute(input: K): V
}

interface SuspendUseCase<K, V> {

    suspend fun execute(input: K): V
}