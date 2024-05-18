package me.vaimon.summarizer.domain.repository

import kotlinx.coroutines.flow.Flow
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity

interface SummarizationRepository {
    fun getSummarizationHistory(): Flow<List<SummarizedTextEntity>>
    suspend fun saveSummarizedText(text: SummarizedTextEntity)
    suspend fun summarizeExtractive(text: String, compressionRate: Float) : String
    suspend fun summarizeAbstractive(text: String) : String
}