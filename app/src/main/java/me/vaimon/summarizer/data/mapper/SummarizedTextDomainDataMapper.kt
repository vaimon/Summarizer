package me.vaimon.summarizer.data.mapper

import me.vaimon.summarizer.data.models.SummarizedTextData
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.util.Mapper
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

class SummarizedTextDomainDataMapper @Inject constructor() :
    Mapper<SummarizedTextEntity, SummarizedTextData> {
    override fun from(e: SummarizedTextData): SummarizedTextEntity {
        return SummarizedTextEntity(
            e.id,
            e.timestamp.let {
                LocalDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault())
            },
            e.originalText,
            e.summarizedText,
            e.compressionRate
        )
    }

    override fun to(t: SummarizedTextEntity): SummarizedTextData {
        return SummarizedTextData(
            t.id,
            t.timestamp.toEpochSecond(OffsetDateTime.now().offset),
            t.originalText,
            t.summarizedText,
            t.compressionRate
        )
    }
}