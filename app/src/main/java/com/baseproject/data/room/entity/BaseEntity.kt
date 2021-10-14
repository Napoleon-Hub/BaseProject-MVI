package com.baseproject.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_table")
data class BaseEntity(
    @PrimaryKey
    val id: Int,
    val score: Int,
    val isWin: Boolean
)