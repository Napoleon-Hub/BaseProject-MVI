package com.baseproject.di

import com.baseproject.domain.local.entity.BaseEntityRepository
import com.baseproject.domain.local.entity.BaseEntityRepositoryImpl
import com.baseproject.data.room.BaseRoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class BaseEntityRepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindRepository(impl: BaseEntityRepositoryImpl) : BaseEntityRepository

    companion object {

        @Provides
        fun provideTrainingDaysDao(db: BaseRoomDatabase) = db.entitiesDao()
    }
}