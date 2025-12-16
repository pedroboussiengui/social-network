package org.example.infra.di

import org.example.application.port.PostRepository
import org.example.application.usecase.CreatePost
import org.example.infra.database.PostgresPostRepository
import org.koin.dsl.module

val appModule = module {

    single<PostRepository> {
        PostgresPostRepository()
    }

    factory {
        CreatePost(postRepository = get())
    }

}