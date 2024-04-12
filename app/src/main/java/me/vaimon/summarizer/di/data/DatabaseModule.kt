package me.vaimon.summarizer.di.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.vaimon.summarizer.data.datasource.db.AppDatabase
import me.vaimon.summarizer.data.datasource.db.dao.SummarizedTextDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, AppDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideSummarizedTextDao(db: AppDatabase): SummarizedTextDao {
        return db.summarizedTextDao()
    }
}