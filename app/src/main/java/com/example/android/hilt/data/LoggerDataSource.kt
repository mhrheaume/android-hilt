package com.example.android.hilt.data

interface LoggerDataSource {
    fun addLog(msg: String)
    fun removeLogs()

    suspend fun getAllLogs(): List<Log>
}