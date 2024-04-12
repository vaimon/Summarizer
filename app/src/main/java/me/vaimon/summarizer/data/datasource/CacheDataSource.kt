package me.vaimon.summarizer.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class CacheDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun saveToCacheFile(content: String): String {
        return withContext(Dispatchers.IO) {
            val outputFile =
                File.createTempFile(UUID.randomUUID().toString(), null, context.cacheDir)
            outputFile.writeText(content)
            return@withContext outputFile.name
        }
    }

    suspend fun readFromCacheFile(filename: String): String {
        val inputFile = File(context.cacheDir, filename)
        return withContext(Dispatchers.IO) { inputFile.readText() }
    }
}