package com.example.translation.di.favorites

import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.favorites.FavoritesInteractor
import com.example.translation.presentation.presenter.favorites.FavoritesPresenter
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
class FavoritesFragmentModule {

    @Provides
    fun provideFavoritesPresenter(
        router: Router,
        interactor: FavoritesInteractor
    ): FavoritesPresenter {
        return FavoritesPresenter(router, interactor)
    }

    @Provides
    fun provideFavoritesInteractor(
        favoritesRepository: TranslationRepository
    ): FavoritesInteractor {
        return FavoritesInteractor(favoritesRepository)
    }
}