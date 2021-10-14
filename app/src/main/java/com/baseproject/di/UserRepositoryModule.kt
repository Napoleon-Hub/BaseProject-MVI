package com.baseproject.di

import com.baseproject.domain.remote.user.UserRepository
import com.baseproject.domain.remote.user.UserRepositoryImpl
import com.baseproject.data.rest.api.UserApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UserRepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindRepository(impl: UserRepositoryImpl): UserRepository

    companion object {

        @Provides
        fun provideRestApi(retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }
    }

}