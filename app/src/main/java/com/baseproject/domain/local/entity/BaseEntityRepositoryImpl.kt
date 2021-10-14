package com.baseproject.domain.local.entity

import androidx.lifecycle.LiveData
import com.baseproject.data.room.dao.BaseEntitiesDao
import com.baseproject.data.room.entity.BaseEntity
import javax.inject.Inject

class BaseEntityRepositoryImpl @Inject constructor(private val dao: BaseEntitiesDao) :
    BaseEntityRepository {

    override suspend fun insertEntity(entity: BaseEntity) = dao.insertEntity(entity)

    override fun getAllEntities(): LiveData<List<BaseEntity>> = dao.getAllEntities()

    override suspend fun deleteAllEntities(): Int = dao.deleteAllEntities()

    override suspend fun getEntitiesCount(): Int = dao.getEntitiesCount()

}