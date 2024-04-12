package me.vaimon.summarizer.domain.entity

import java.time.LocalDateTime

data class SummarizedTextEntity(
    val id: Long = 0,
    val timestamp: LocalDateTime,
    val originalText: String,
    val summarizedText: String,
    val compressionRate: Int
)