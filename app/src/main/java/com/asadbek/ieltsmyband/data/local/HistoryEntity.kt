package com.asadbek.ieltsmyband.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val title: String,
    val score: String,
    val timestamp: Long = System.currentTimeMillis()
)
