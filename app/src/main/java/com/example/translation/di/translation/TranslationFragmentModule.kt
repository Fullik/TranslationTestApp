package com.example.translation.di.translation

import com.example.translation.domain.translation.TranslationInteractor
import com.example.translation.domain.translation.TranslationInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface TranslationFragmentModule {

    @Binds
    fun provideTranslationInteractor(interactor: TranslationInteractorImpl): TranslationInteractor
}