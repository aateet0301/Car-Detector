package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarScanDao {
    @Query("SELECT * FROM car_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<CarScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: CarScanEntity): Long

    @Delete
    suspend fun deleteScan(scan: CarScanEntity)

    @Query("DELETE FROM car_scans")
    suspend fun clearAll()
}
