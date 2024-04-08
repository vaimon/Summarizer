package me.vaimon.summarizer.domain.usecase

import me.vaimon.summarizer.domain.repository.CacheRepository
import javax.inject.Inject

class SaveInputTextUseCase @Inject constructor(
    private val cacheRepository: CacheRepository
) {
    suspend operator fun invoke(inputText: String): String =
        cacheRepository.saveInputTextToCache(inputText)
}