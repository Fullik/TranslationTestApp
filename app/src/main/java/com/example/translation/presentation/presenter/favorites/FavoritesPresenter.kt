package com.example.translation.presentation.presenter.favorites

import com.example.translation.domain.favorites.FavoritesInteractor
import com.example.translation.domain.model.FavoriteTranslationModel
import com.example.translation.presentation.presenter.base.BasePresenter
import com.example.translation.presentation.view.favorites.FavoritesView
import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers

class FavoritesPresenter(
    private val router: Router,
    private val interactor: FavoritesInteractor
) : BasePresenter<FavoritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getAllFavoriteTranslations()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.showFavorites(it) },
                { viewState.showFavoriteTranslationsFetchError() }
            )
            .disposeOnDestroy()
    }

    fun removeFavoriteTranslation(model: FavoriteTranslationModel, position: Int) {
        interactor.removeFavoriteTranslation(model)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.showItemDeletedMessage() },
                { viewState.restoreItem(model, position) }
            )
            .disposeOnDestroy()
    }

    fun onBackPressed() {
        router.exit()
    }
}