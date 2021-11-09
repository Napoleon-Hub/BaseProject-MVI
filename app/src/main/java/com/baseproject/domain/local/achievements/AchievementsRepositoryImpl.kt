package com.baseproject.domain.local.achievements

import com.baseproject.data.room.dao.AchievementsDao
import com.baseproject.data.room.entity.AchievementsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementsRepositoryImpl  @Inject constructor(private val dao: AchievementsDao) :
    AchievementsRepository {

    override suspend fun insertAchievements(achievements: List<AchievementsEntity>) = dao.insertAchievements(achievements)

    override fun getAllAchievements(): Flow<List<AchievementsEntity>> = dao.getAllAchievements()

    override fun getAchieveById(stringName: String): AchievementsEntity = dao.getAchieveById(stringName)

    override suspend fun getEntitiesCount() = dao.getEntitiesCount()

    override suspend fun updateReceipt(stringName : String, achieveReceived: Boolean) = dao.updateReceipt(stringName, achieveReceived)

}