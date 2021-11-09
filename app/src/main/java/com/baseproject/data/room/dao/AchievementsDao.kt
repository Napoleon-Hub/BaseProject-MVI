package com.baseproject.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baseproject.data.room.entity.AchievementsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(achievements: List<AchievementsEntity>)

    @Query("SELECT * FROM achievements_table")
    fun getAllAchievements(): Flow<List<AchievementsEntity>>

    @Query("SELECT * FROM achievements_table WHERE stringName = :stringName")
    fun getAchieveById(stringName : String): AchievementsEntity

    @Query("SELECT COUNT(*) FROM achievements_table")
    suspend fun getEntitiesCount(): Int

    @Query("UPDATE achievements_table SET achieveReceived = :achieveReceived WHERE stringName = :stringName")
    suspend fun updateReceipt(stringName : String, achieveReceived: Boolean)

}