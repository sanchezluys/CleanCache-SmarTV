package com.example.model

data class DeviceSpecs(
    val processorName: String,
    val cpuCores: Int,
    val cpuArchitecture: String,
    val totalRamBytes: Long,
    val availRamBytes: Long,
    val usedRamBytes: Long,
    val ramUsagePercent: Float,
    val totalRomBytes: Long,
    val availRomBytes: Long,
    val usedRomBytes: Long,
    val romUsagePercent: Float
)

data class StorageStatus(
    val totalBytes: Long,
    val availableBytes: Long,
    val usedBytes: Long,
    val usagePercent: Float
)

data class CleanResult(
    val freedBytes: Long,
    val previousFreeBytes: Long,
    val newFreeBytes: Long,
    val timestamp: Long = System.currentTimeMillis()
)
