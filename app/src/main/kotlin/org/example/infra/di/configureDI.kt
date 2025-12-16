package org.example.infra.di

import org.example.application.port.PostRepository
import org.example.application.port.ProfileRepository
import org.example.application.usecase.CreatePost
import org.example.application.usecase.FindPostById
import org.example.infra.database.postgres.PostgresPostRepository
import org.example.infra.database.postgres.PostgresProfileRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PostRepository> {
        PostgresPostRepository()
    }

    single<ProfileRepository> {
        PostgresProfileRepository()
    }
}

val useCaseModule = module {
    factory {
        CreatePost(postRepository = get())
    }

    factory {
        FindPostById(postRepository = get())
    }
}