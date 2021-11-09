package com.baseproject.domain.local.achievements

import com.baseproject.data.room.entity.AchievementsEntity
import kotlinx.coroutines.flow.Flow

interface AchievementsRepository {

    suspend fun insertAchievements(achievements: List<AchievementsEntity>)

    fun getAllAchievements(): Flow<List<AchievementsEntity>>

    fun getAchieveById(stringName: String): AchievementsEntity

    suspend fun getEntitiesCount(): Int

    suspend fun updateReceipt(stringName : String, achieveReceived: Boolean)

}