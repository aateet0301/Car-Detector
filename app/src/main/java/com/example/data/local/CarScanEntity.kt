package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_scans")
data class CarScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val makeModel: String,
    val yearGen: String,
    val exteriorColor: String,
    val colorFinish: String = "High-Gloss Solid",
    val colorCode: String = "N/A",
    val colorHex: String = "#0077C8",
    val colorTone: String = "",
    val colorAccents: String = "",
    val bodyStyle: String,
    val enginePowertrain: String,
    val drivetrain: String,
    val horsepower: String,
    val notableFeatures: String,
    val confidenceLevel: String,
    val rawResponse: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null,
    val sampleDrawableRes: Int? = null,
    val isCarDetected: Boolean = true
)
