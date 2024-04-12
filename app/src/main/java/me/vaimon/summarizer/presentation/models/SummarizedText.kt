package me.vaimon.summarizer.presentation.models

import java.time.LocalDateTime

data class SummarizedText(
    val id: Long,
    val timestamp: LocalDateTime,
    val originalText: String,
    val summarizedText: String,
    val compressionRate: Int
)
