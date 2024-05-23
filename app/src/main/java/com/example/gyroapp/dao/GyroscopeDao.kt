package com.example.gyroapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GyroscopeDataDao {
    @Query("SELECT * FROM gyroscope_data ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<GyroscopeData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: GyroscopeData)

    @Update
    suspend fun update(data: GyroscopeData)

    @Query("SELECT * FROM gyroscope_data ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): GyroscopeData?

}