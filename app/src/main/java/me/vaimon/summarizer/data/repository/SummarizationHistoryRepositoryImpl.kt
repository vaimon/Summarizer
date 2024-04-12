package me.vaimon.summarizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.vaimon.summarizer.data.datasource.db.dao.SummarizedTextDao
import me.vaimon.summarizer.data.models.SummarizedTextData
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.domain.repository.SummarizationHistoryRepository
import me.vaimon.summarizer.util.Mapper
import javax.inject.Inject

class SummarizationHistoryRepositoryImpl @Inject constructor(
    private val summarizedTextDao: SummarizedTextDao,
    private val summarizedTextMapper: Mapper<SummarizedTextEntity, SummarizedTextData>
) : SummarizationHistoryRepository {
    override fun getSummarizationHistory(): Flow<List<SummarizedTextEntity>> =
        summarizedTextDao.getAllSummarizedTexts().map { list ->
            list.map { summarizedTextMapper.from(it) }
        }

    override suspend fun saveSummarizedText(text: SummarizedTextEntity) =
        withContext(Dispatchers.IO) {
            summarizedTextDao.insertSummarizedText(summarizedTextMapper.to(text))
        }

}