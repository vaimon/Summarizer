package me.vaimon.summarizer.domain.usecase

import me.vaimon.summarizer.domain.repository.CacheRepository
import javax.inject.Inject

class ReadInputTextUseCase @Inject constructor(
    private val cacheRepository: CacheRepository
) {
    suspend operator fun invoke(path: String): String =
        cacheRepository.readTextFromCache(path)
}