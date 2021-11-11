package com.baseproject.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baseproject.domain.enums.Difficulty

@Entity(tableName = "entity_table")
data class BaseEntity(
    @PrimaryKey
    val id: Int,
    val score: Int,
    val isWin: Boolean,
    val difficulty: Int = 0,
    val status: Int = 0
)