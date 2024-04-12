package me.vaimon.summarizer.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.vaimon.summarizer.data.datasource.db.dao.SummarizedTextDao
import me.vaimon.summarizer.data.models.SummarizedTextData

@Database(
    entities = [SummarizedTextData::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun summarizedTextDao(): SummarizedTextDao

    companion object {
        const val DATABASE_NAME = "SummarizerDB"
    }
}