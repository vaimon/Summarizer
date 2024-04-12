package me.vaimon.summarizer.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.domain.repository.SummarizationHistoryRepository
import javax.inject.Inject

class GetSummarizationHistoryUseCase @Inject constructor(
    private val summarizationHistoryRepository: SummarizationHistoryRepository
) {
    operator fun invoke(): Flow<List<SummarizedTextEntity>> =
        summarizationHistoryRepository.getSummarizationHistory()
}