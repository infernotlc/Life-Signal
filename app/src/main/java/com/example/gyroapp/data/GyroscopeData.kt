package com.example.gyroapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gyroscope_data")
data class GyroscopeData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long
)
