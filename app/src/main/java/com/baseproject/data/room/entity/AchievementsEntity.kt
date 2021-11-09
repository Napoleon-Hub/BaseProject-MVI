package com.baseproject.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements_table")
class AchievementsEntity(
    @PrimaryKey
    val stringName : String,
    val drawableId: Int,
    val achieveTitleId: Int,
    val achieveDescriptionId: Int,
    val achieveReceived: Boolean = false
)