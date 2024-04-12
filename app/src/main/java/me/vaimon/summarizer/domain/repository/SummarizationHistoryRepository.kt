package me.vaimon.summarizer.domain.repository

import kotlinx.coroutines.flow.Flow
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity

interface SummarizationHistoryRepository {
    fun getSummarizationHistory(): Flow<List<SummarizedTextEntity>>
    suspend fun saveSummarizedText(text: SummarizedTextEntity)
}