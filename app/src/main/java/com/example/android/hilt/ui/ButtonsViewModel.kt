package com.example.android.hilt.ui

import androidx.lifecycle.ViewModel
import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.di.DatabaseLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ButtonsViewModel @Inject constructor(
    @DatabaseLogger private val logger: LoggerDataSource
): ViewModel() {
    fun handleButtonClick(button: Int) {
        logger.addLog("Interaction with 'Button ${button}'")
    }

    fun handleDeleteLogs() {
        logger.removeLogs()
    }
}