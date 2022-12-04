package com.example.android.hilt.contentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.example.android.hilt.data.LogDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class LogsContentProvider : ContentProvider() {
    private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, LOGS_TABLE, CODE_LOGS_DIR)
        addURI(AUTHORITY, "$LOGS_TABLE/*", CODE_LOGS_ITEM)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = matcher.match(uri)

        if (!(code == CODE_LOGS_DIR || code == CODE_LOGS_ITEM)) {
            throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }

        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val logDao = getLogDao(appContext)

        val cursor = if (code == CODE_LOGS_DIR) {
            logDao.selectAllLogsCursor()
        } else {
            logDao.selectLogById(ContentUris.parseId(uri))
        }

        cursor.setNotificationUri(appContext.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? =
        throw UnsupportedOperationException("Only reading operations are allowed")

    override fun insert(uri: Uri, values: ContentValues?): Uri? =
        throw UnsupportedOperationException("Only reading operations are allowed")

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int =
        throw UnsupportedOperationException("Only reading operations are allowed")

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int =
        throw UnsupportedOperationException("Only reading operations are allowed")

    private fun getLogDao(appContext: Context): LogDao {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            LogsContentProviderEntryPoint::class.java
        )

        return hiltEntryPoint.logDao()
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface LogsContentProviderEntryPoint {
        fun logDao(): LogDao
    }

    companion object {
        private const val LOGS_TABLE = "logs"
        private const val AUTHORITY = "com.example.android.hilt.provider"
        private const val CODE_LOGS_DIR = 1
        private const val CODE_LOGS_ITEM = 2
    }
}