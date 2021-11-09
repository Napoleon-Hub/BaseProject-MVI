package com.baseproject.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baseproject.data.room.dao.AchievementsDao
import com.baseproject.data.room.dao.BaseEntitiesDao
import com.baseproject.data.room.entity.AchievementsEntity
import com.baseproject.data.room.entity.BaseEntity

@Database(entities = [BaseEntity::class, AchievementsEntity::class], exportSchema = true, version = 3)
abstract class BaseRoomDatabase : RoomDatabase() {
    abstract fun entitiesDao(): BaseEntitiesDao
    abstract fun achievementsDao(): AchievementsDao

    companion object {
        @Volatile
        private var INSTANCE: BaseRoomDatabase? = null

        fun getDatabase(context: Context): BaseRoomDatabase {
            INSTANCE?.let { return it }
            synchronized(this) {
                return Room.databaseBuilder(context, BaseRoomDatabase::class.java, "base_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}