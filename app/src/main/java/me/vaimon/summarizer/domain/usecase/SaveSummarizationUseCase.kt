package me.vaimon.summarizer.domain.usecase

import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.domain.repository.SummarizationRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SaveSummarizationUseCase @Inject constructor(
    private val summarizationRepository: SummarizationRepository
) {
    suspend operator fun invoke(
        originalText: String,
        summarizedText: String,
        compressionRate: Int
    ) =
        summarizationRepository.saveSummarizedText(
            SummarizedTextEntity(
                timestamp = LocalDateTime.now(),
                originalText = originalText,
                summarizedText = summarizedText,
                compressionRate = compressionRate
            )
        )
}