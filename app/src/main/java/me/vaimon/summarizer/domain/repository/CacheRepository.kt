package me.vaimon.summarizer.domain.repository

interface CacheRepository {
    suspend fun saveTextToCache(inputText: String): String
    suspend fun readTextFromCache(path: String): String
}