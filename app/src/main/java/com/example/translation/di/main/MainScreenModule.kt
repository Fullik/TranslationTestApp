package com.example.translation.di.main

import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.data.repository.translation.TranslationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface MainScreenModule {

    @Binds
    @Reusable
    fun provideTranslationRepository(repository: TranslationRepositoryImpl): TranslationRepository
}