package com.example.gyroapp

import androidx.lifecycle.LiveData
import com.example.gyroapp.data.GyroscopeData
import com.example.gyroapp.data.GyroscopeDataDao


class GyroscopeRepository(private val gyroscopeDataDao: GyroscopeDataDao) {

    val allGyroscopeData: LiveData<List<GyroscopeData>> = gyroscopeDataDao.getAll()

    suspend fun insertGyroscopeData(data: GyroscopeData) {
        gyroscopeDataDao.insert(data)
    }

    suspend fun updateGyroscopeData(data: GyroscopeData) {
        gyroscopeDataDao.update(data)
    }

    suspend fun getLatestGyroscopeData(): GyroscopeData? {
        return gyroscopeDataDao.getLatest()
    }
}
