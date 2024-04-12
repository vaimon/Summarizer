package me.vaimon.summarizer.data.repository

import me.vaimon.summarizer.data.datasource.CacheDataSource
import me.vaimon.summarizer.domain.repository.CacheRepository
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
    private val cacheDataSource: CacheDataSource
) : CacheRepository {
    override suspend fun saveTextToCache(inputText: String) =
        cacheDataSource.saveToCacheFile(inputText)

    override suspend fun readTextFromCache(filename: String) =
        cacheDataSource.readFromCacheFile(filename)
}