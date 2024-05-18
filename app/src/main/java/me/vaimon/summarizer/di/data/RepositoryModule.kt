package me.vaimon.summarizer.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.vaimon.summarizer.data.repository.CacheRepositoryImpl
import me.vaimon.summarizer.data.repository.SummarizationRepositoryImpl
import me.vaimon.summarizer.domain.repository.CacheRepository
import me.vaimon.summarizer.domain.repository.SummarizationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCacheRepository(repo: CacheRepositoryImpl): CacheRepository = repo

    @Provides
    @Singleton
    fun provideSummarizationHistoryRepository(repo: SummarizationRepositoryImpl): SummarizationRepository =
        repo

}