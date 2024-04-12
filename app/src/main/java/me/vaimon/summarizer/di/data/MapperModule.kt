package me.vaimon.summarizer.di.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.vaimon.summarizer.data.mapper.SummarizedTextDomainDataMapper
import me.vaimon.summarizer.data.models.SummarizedTextData
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.util.Mapper

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {

    @Binds
    fun bindSummarizedTextMapper(mapper: SummarizedTextDomainDataMapper): Mapper<SummarizedTextEntity, SummarizedTextData>
}