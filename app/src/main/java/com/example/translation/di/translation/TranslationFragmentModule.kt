package com.example.translation.di.translation

import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.ResourceManager
import com.example.translation.domain.translation.TranslationInteractor
import com.example.translation.presentation.presenter.translation.TranslationPresenter
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class TranslationFragmentModule {

    @Provides
    fun provideTranslationPresenter(
        interactor: TranslationInteractor,
        router: Router
    ): TranslationPresenter {
        return TranslationPresenter(interactor, router)
    }

    @Provides
    fun provideTranslationInteractor(
        repository: TranslationRepository,
        resourceManager: ResourceManager
    ): TranslationInteractor {
        return TranslationInteractor(repository, resourceManager)
    }
}