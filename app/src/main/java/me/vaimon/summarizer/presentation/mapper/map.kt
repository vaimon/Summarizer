package me.vaimon.summarizer.presentation.mapper

import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.presentation.models.SummarizedText
import me.vaimon.summarizer.util.Mapper
import javax.inject.Inject

class SummarizedTextAppDomainMapper @Inject constructor() :
    Mapper<SummarizedText, SummarizedTextEntity> {
    override fun from(e: SummarizedTextEntity): SummarizedText {
        return SummarizedText(
            e.id,
            e.timestamp,
            e.originalText,
            e.summarizedText,
            e.compressionRate
        )
    }

    override fun to(t: SummarizedText): SummarizedTextEntity {
        throw IllegalStateException()
    }
}