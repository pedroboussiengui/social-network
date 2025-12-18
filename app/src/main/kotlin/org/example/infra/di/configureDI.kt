package org.example.infra.di

import org.example.application.port.CommentRepository
import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import org.example.application.port.ProfileRepository
import org.example.application.usecase.CommentPost
import org.example.application.usecase.CreatePost
import org.example.application.usecase.CreateProfile
import org.example.application.usecase.FindPostById
import org.example.application.usecase.ToggleLikePost
import org.example.infra.database.postgres.PostgresCommentRepository
import org.example.infra.database.postgres.PostgresLikeRepository
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

    single<CommentRepository> {
        PostgresCommentRepository()
    }

    single<LikeRepository> {
        PostgresLikeRepository()
    }
}

val useCaseModule = module {
    factory { CreateProfile(profileRepository = get()) }
    factory { CreatePost(postRepository = get()) }
    factory { FindPostById(postRepository = get(), likeRepository = get()) }
    factory { CommentPost(commentRepository = get()) }
    factory { ToggleLikePost(likeRepository = get(), profileRepository = get(), postRepository = get()) }
}