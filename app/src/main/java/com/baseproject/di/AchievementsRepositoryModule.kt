package com.baseproject.di

import com.baseproject.data.room.BaseRoomDatabase
import com.baseproject.domain.local.achievements.AchievementsRepository
import com.baseproject.domain.local.achievements.AchievementsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AchievementsRepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindRepository(impl: AchievementsRepositoryImpl) : AchievementsRepository

    companion object {

        @Provides
        fun provideAchievementsDao(db: BaseRoomDatabase) = db.achievementsDao()
    }

}