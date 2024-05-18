package me.vaimon.summarizer.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.domain.repository.SummarizationRepository
import javax.inject.Inject

class GetSummarizationHistoryUseCase @Inject constructor(
    private val summarizationRepository: SummarizationRepository
) {
    operator fun invoke(): Flow<List<SummarizedTextEntity>> =
        summarizationRepository.getSummarizationHistory()
}