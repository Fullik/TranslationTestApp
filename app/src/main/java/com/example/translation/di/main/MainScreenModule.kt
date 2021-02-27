package com.example.translation.di.main

import com.example.translation.data.database.TranslationDao
import com.example.translation.data.network.TranslationApi
import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.di.qualifiers.DatabaseScheduler
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.Scheduler

@Module
class MainScreenModule {

    @Provides
    @Reusable
    fun provideTranslationRepository(
        translation: TranslationDao,
        translationApi: TranslationApi,
        @DatabaseScheduler
        databaseScheduler: Scheduler
    ): TranslationRepository {
        return TranslationRepository(translation, translationApi, databaseScheduler)
    }
}