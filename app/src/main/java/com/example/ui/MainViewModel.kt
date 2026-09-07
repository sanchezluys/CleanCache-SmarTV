package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CleanResult
import com.example.model.DeviceSpecs
import com.example.model.StorageStatus
import com.example.system.CacheCleaner
import com.example.system.SystemInfoProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class MainUiState(
    val deviceSpecs: DeviceSpecs? = null,
    val storageStatus: StorageStatus? = null,
    val cpuUsagePercent: Float = 0f,
    val isCleaning: Boolean = false,
    val lastCleanResult: CleanResult? = null,
    val showResultDialog: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val systemInfoProvider = SystemInfoProvider(application)
    private val cacheCleaner = CacheCleaner(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var metricsJob: Job? = null

    init {
        loadInitialData()
        startMetricsMonitoring()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            val specs = systemInfoProvider.getDeviceSpecs()
            val storage = systemInfoProvider.getStorageStatus()
            val cpu = systemInfoProvider.getCpuUsagePercent()
            _uiState.update {
                it.copy(
                    deviceSpecs = specs,
                    storageStatus = storage,
                    cpuUsagePercent = cpu
                )
            }
        }
    }

    private fun startMetricsMonitoring() {
        metricsJob?.cancel()
        metricsJob = viewModelScope.launch {
            while (isActive) {
                delay(2500)
                if (!_uiState.value.isCleaning) {
                    val cpu = systemInfoProvider.getCpuUsagePercent()
                    val storage = systemInfoProvider.getStorageStatus()
                    _uiState.update {
                        it.copy(
                            cpuUsagePercent = cpu,
                            storageStatus = storage
                        )
                    }
                }
            }
        }
    }

    fun onCleanCacheClicked() {
        if (_uiState.value.isCleaning) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCleaning = true) }

            val result = cacheCleaner.executeClean()

            // Refresh specs and storage
            val updatedSpecs = systemInfoProvider.getDeviceSpecs()
            val updatedStorage = systemInfoProvider.getStorageStatus()
            val updatedCpu = systemInfoProvider.getCpuUsagePercent()

            _uiState.update {
                it.copy(
                    isCleaning = false,
                    deviceSpecs = updatedSpecs,
                    storageStatus = updatedStorage,
                    cpuUsagePercent = updatedCpu,
                    lastCleanResult = result,
                    showResultDialog = true
                )
            }
        }
    }

    fun dismissResultDialog() {
        _uiState.update { it.copy(showResultDialog = false) }
    }

    override fun onCleared() {
        super.onCleared()
        metricsJob?.cancel()
    }
}
