package me.vaimon.summarizer.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.vaimon.summarizer.data.models.SummarizedTextData

@Dao
interface SummarizedTextDao {
    @Insert
    suspend fun insertSummarizedText(text: SummarizedTextData)

    @Query("SELECT * FROM summarized_texts")
    fun getAllSummarizedTexts(): Flow<List<SummarizedTextData>>
}