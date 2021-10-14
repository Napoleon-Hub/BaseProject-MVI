package com.baseproject.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baseproject.data.room.entity.BaseEntity

@Dao
interface BaseEntitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: BaseEntity)

    @Query("SELECT * FROM entity_table")
    fun getAllEntities(): LiveData<List<BaseEntity>>

    @Query("DELETE FROM entity_table")
    suspend fun deleteAllEntities(): Int

    @Query("SELECT COUNT(*) FROM entity_table")
    suspend fun getEntitiesCount(): Int
}