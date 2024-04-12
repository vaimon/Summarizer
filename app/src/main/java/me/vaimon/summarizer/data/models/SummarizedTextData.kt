package me.vaimon.summarizer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summarized_texts")
data class SummarizedTextData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val originalText: String,
    val summarizedText: String,
    val compressionRate: Int
)