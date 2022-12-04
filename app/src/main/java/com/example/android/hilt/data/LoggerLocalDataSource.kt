/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
@Singleton
class LoggerLocalDataSource @Inject constructor(
    private val logDao: LogDao,
    private val coroutineScope: CoroutineScope
) : LoggerDataSource {

    override fun addLog(msg: String) {
        coroutineScope.launch {
            logDao.insertAll(
                Log(
                    msg,
                    System.currentTimeMillis()
                )
            )
        }
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        coroutineScope.launch {
            val logs = logDao.getAll()
            withContext(Dispatchers.Main) {
                callback(logs)
            }
        }
    }

    override fun removeLogs() {
        coroutineScope.launch {
            logDao.nukeTable()
        }
    }
}
