package org.example.infra.di

import org.example.application.port.CommentRepository
import org.example.application.port.FollowRepository
import org.example.application.port.LikeRepository
import org.example.application.port.PostRepository
import org.example.application.port.ProfileRepository
import org.example.application.usecase.comment.CommentPost
import org.example.application.usecase.post.CreatePost
import org.example.application.usecase.profile.CreateProfile
import org.example.application.usecase.post.FindPostById
import org.example.application.usecase.post.ToggleLikePost
import org.example.application.usecase.comment.ListPostComments
import org.example.application.usecase.post.DeletePost
import org.example.application.usecase.post.PrivatePost
import org.example.application.usecase.post.PublicPost
import org.example.application.usecase.profile.DeleteProfile
import org.example.application.usecase.profile.DownloadAvatarProfile
import org.example.application.usecase.profile.GetProfileByUsername
import org.example.application.usecase.profile.ListProfilePosts
import org.example.application.usecase.profile.PrivateProfile
import org.example.application.usecase.profile.PublicProfile
import org.example.application.usecase.profile.ToggleFollowProfile
import org.example.application.usecase.profile.UploadAvatarProfile
import org.example.infra.database.postgres.PostgresCommentRepository
import org.example.infra.database.postgres.PostgresFollowRepository
import org.example.infra.database.postgres.PostgresLikeRepository
import org.example.infra.database.postgres.PostgresPostRepository
import org.example.infra.database.postgres.PostgresProfileRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PostRepository> { PostgresPostRepository() }
    single<ProfileRepository> { PostgresProfileRepository() }
    single<CommentRepository> { PostgresCommentRepository() }
    single<LikeRepository> { PostgresLikeRepository() }
    single<FollowRepository> { PostgresFollowRepository() }

}

val useCaseModule = module {
    factory { GetProfileByUsername(profileRepository = get(), followRepository = get()) }
    factory { CreateProfile(profileRepository = get()) }
    factory { DeleteProfile(profileRepository = get()) }
    factory { ListProfilePosts(postRepository = get(), likeRepository = get(), profileRepository = get(), followRepository = get()) }
    factory { UploadAvatarProfile(profileRepository = get()) }
    factory { DownloadAvatarProfile() }
    factory { ToggleFollowProfile(followRepository = get()) }
    factory { PrivateProfile(profileRepository = get()) }
    factory { PublicProfile(profileRepository = get()) }

    factory { CreatePost(postRepository = get(), profileRepository = get()) }
    factory { DeletePost(postRepository = get()) }
    factory { PrivatePost(postRepository = get()) }
    factory { PublicPost(postRepository = get()) }
    factory { FindPostById(postRepository = get(), likeRepository = get()) }
    factory { ToggleLikePost(likeRepository = get(), profileRepository = get(), postRepository = get()) }

    factory { CommentPost(commentRepository = get()) }
    factory { ListPostComments(commentRepository = get()) }
}