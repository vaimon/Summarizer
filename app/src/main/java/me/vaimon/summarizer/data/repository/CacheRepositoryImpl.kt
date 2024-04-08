package me.vaimon.summarizer.data.repository

import me.vaimon.summarizer.data.datasource.db.CacheDataSource
import me.vaimon.summarizer.domain.repository.CacheRepository
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
    private val cacheDataSource: CacheDataSource
) : CacheRepository {
    override suspend fun saveInputTextToCache(inputText: String) =
        cacheDataSource.saveToCacheFile(inputText)

    override suspend fun readTextFromCache(path: String) =
        cacheDataSource.readFromCacheFile(path)
}