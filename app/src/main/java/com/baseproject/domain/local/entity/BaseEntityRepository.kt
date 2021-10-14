package com.baseproject.domain.local.entity

import androidx.lifecycle.LiveData
import com.baseproject.data.room.entity.BaseEntity

interface BaseEntityRepository {

    suspend fun insertEntity(entity: BaseEntity)

    fun getAllEntities(): LiveData<List<BaseEntity>>

    suspend fun deleteAllEntities(): Int

    suspend fun getEntitiesCount(): Int
}