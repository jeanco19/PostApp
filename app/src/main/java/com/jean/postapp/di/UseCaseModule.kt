package com.jean.postapp.di

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.repository.user.UserRepository
import com.jean.postapp.domain.usecase.post.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideInsertFavoritePostUseCase(postRepository: PostRepository): InsertFavoritePostUseCase {
        return InsertFavoritePostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideInsertSeenPostUseCase(postRepository: PostRepository): InsertSeenPostUseCase {
        return InsertSeenPostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetAllPostUseCase(postRepository: PostRepository): GetAllPostUseCase {
        return GetAllPostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetFavoritePostUseCase(postRepository: PostRepository): GetFavoritePostUseCase {
        return GetFavoritePostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetPostDetailUseCase(
        postRepository: PostRepository,
        userRepository: UserRepository
    ): GetPostDetailUseCase {
        return GetPostDetailUseCaseImpl(postRepository, userRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRemoveFavoritePostUseCase(postRepository: PostRepository): RemoveFavoritePostUseCase {
        return RemoveFavoritePostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRemovePostUseCase(postRepository: PostRepository): RemovePostUseCase {
        return RemovePostUseCaseImpl(postRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRemoveAllPostUseCase(postRepository: PostRepository): RemoveAllPostUseCase {
        return RemoveAllPostUseCaseImpl(postRepository)
    }
}