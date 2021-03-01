package com.example.translation.di

import android.content.Context
import androidx.room.Room
import com.example.translation.data.database.AppDatabase
import com.example.translation.data.database.TranslationDao
import com.example.translation.data.database.migration.Migration1To2
import com.example.translation.di.qualifiers.DatabaseScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(Migration1To2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTranslationDao(database: AppDatabase): TranslationDao {
        return database.translationDao()
    }

    @Provides
    @Singleton
    @DatabaseScheduler
    fun provideDatabaseScheduler(): Scheduler {
        return Schedulers.io()
    }

    private companion object {
        private const val DATABASE_NAME = "tl_database"
    }
}