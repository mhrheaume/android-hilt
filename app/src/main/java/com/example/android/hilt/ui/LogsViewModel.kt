package com.example.android.hilt.ui

import androidx.lifecycle.ViewModel
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.di.DatabaseLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    @DatabaseLogger private val logger: LoggerDataSource
): ViewModel() {
    private val _logs = MutableStateFlow<List<Log>>(emptyList())

    val logs: StateFlow<List<Log>> = _logs.asStateFlow()

    init {
        logger.getAllLogs {
            _logs.value = it
        }
    }
}