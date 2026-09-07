package com.example.system

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import com.example.model.CleanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class CacheCleaner(private val context: Context) {

    suspend fun executeClean(): CleanResult = withContext(Dispatchers.IO) {
        val statBefore = StatFs(Environment.getDataDirectory().path)
        val initialFreeBytes = statBefore.availableBytes

        var localFreedBytes = 0L

        // 1. Purge application's own internal & external cache
        localFreedBytes += clearDirectory(context.cacheDir)
        localFreedBytes += clearDirectory(context.codeCacheDir)
        context.externalCacheDir?.let {
            localFreedBytes += clearDirectory(it)
        }

        // 2. Request system cache purge via official Android StorageManager API
        requestSystemPurge()

        // 3. Trigger runtime memory reclamation
        System.gc()

        // 4. Brief pause to let system async file purges settle
        delay(1200)

        // 5. Measure space after clean
        val statAfter = StatFs(Environment.getDataDirectory().path)
        val finalFreeBytes = statAfter.availableBytes

        val systemFreed = finalFreeBytes - initialFreeBytes
        // If the OS reclaimed space, use the real storage delta; otherwise at least reflect cleared local cache
        val totalFreed = if (systemFreed > 0) {
            systemFreed
        } else {
            localFreedBytes.coerceAtLeast(0L)
        }

        CleanResult(
            freedBytes = totalFreed,
            previousFreeBytes = initialFreeBytes,
            newFreeBytes = maxOf(initialFreeBytes, finalFreeBytes)
        )
    }

    private fun requestSystemPurge() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        try {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as? StorageManager
                ?: return

            val uuid = try {
                storageManager.getUuidForPath(context.filesDir)
            } catch (_: Exception) {
                StorageManager.UUID_DEFAULT
            }

            val allocatableBytes = try {
                storageManager.getAllocatableBytes(uuid)
            } catch (_: Exception) {
                0L
            }

            if (allocatableBytes > 0) {
                // Request Android OS to purge caches to free allocatable bytes
                // Cap to 1.5 GB per request to prevent system timeout
                val requestTarget = allocatableBytes.coerceAtMost(1536L * 1024 * 1024)
                try {
                    storageManager.allocateBytes(uuid, requestTarget)
                } catch (_: IOException) {
                    // If target was too ambitious, try smaller chunks
                    try {
                        val fallbackTarget = (requestTarget / 2).coerceAtLeast(64L * 1024 * 1024)
                        storageManager.allocateBytes(uuid, fallbackTarget)
                    } catch (_: Exception) {
                        // Handled gracefully
                    }
                }
            }
        } catch (_: Exception) {
            // Safety fallback
        }
    }

    private fun clearDirectory(dir: File?): Long {
        if (dir == null || !dir.exists()) return 0L
        var bytesFreed = 0L
        try {
            val files = dir.listFiles() ?: return 0L
            for (file in files) {
                bytesFreed += calculateSize(file)
                file.deleteRecursively()
            }
        } catch (_: Exception) {
            // Ignore individual file lock errors
        }
        return bytesFreed
    }

    private fun calculateSize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        var size = 0L
        val children = file.listFiles() ?: return 0L
        for (child in children) {
            size += calculateSize(child)
        }
        return size
    }
}
