package me.vaimon.summarizer.di.app

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.presentation.mapper.SummarizedTextAppDomainMapper
import me.vaimon.summarizer.presentation.models.SummarizedText
import me.vaimon.summarizer.util.Mapper

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {

    @Binds
    fun bindSummarizedTextMapper(mapper: SummarizedTextAppDomainMapper): Mapper<SummarizedText, SummarizedTextEntity>
}