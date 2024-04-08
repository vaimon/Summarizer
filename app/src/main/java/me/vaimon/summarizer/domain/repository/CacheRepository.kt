package me.vaimon.summarizer.domain.repository

interface CacheRepository {
    suspend fun saveInputTextToCache(inputText: String): String
    suspend fun readTextFromCache(path: String): String
}