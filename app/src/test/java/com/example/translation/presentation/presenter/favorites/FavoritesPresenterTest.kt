package com.example.translation.presentation.presenter.favorites

import com.example.translation.domain.favorites.FavoritesInteractor
import com.example.translation.domain.model.FavoriteTranslationModel
import com.example.translation.presentation.view.favorites.FavoritesView
import com.example.translation.presentation.view.favorites.`FavoritesView$$State`
import com.github.terrakok.cicerone.Router
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FavoritesPresenterTest {
    private lateinit var presenter: FavoritesPresenter
    private lateinit var router: Router
    private lateinit var interactor: FavoritesInteractor
    private lateinit var viewState: `FavoritesView$$State`

    @Before
    fun setup() {
        router = mock()
        interactor = mock()
        viewState = mock()
        presenter = FavoritesPresenter(router, interactor)
        presenter.setViewState(viewState)
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `setup view on first view attach success`() {
        val view = mock<FavoritesView>()
        val favorites = getFavoritesTranslations()
        whenever(interactor.getAllFavoriteTranslations())
            .thenReturn(Single.just(favorites))

        presenter.attachView(view)

        verify(viewState).showFavorites(favorites)
        verify(viewState, never()).showFavoriteTranslationsFetchError()
    }

    @Test
    fun `setup view on first view attach error`() {
        val view = mock<FavoritesView>()
        val favorites = getFavoritesTranslations()
        whenever(interactor.getAllFavoriteTranslations())
            .thenReturn(Single.error(IOException()))

        presenter.attachView(view)

        verify(viewState, never()).showFavorites(favorites)
        verify(viewState).showFavoriteTranslationsFetchError()
    }

    @Test
    fun `on back pressed`() {
        presenter.onBackPressed()
        verify(router).exit()
    }

    @Test
    fun `remove favorite translation success`() {
        val model = getFavoritesTranslations().first()
        whenever(interactor.removeFavoriteTranslation(model))
            .thenReturn(Completable.complete())

        presenter.removeFavoriteTranslation(model, 0)

        verify(viewState).showItemDeletedMessage()
        verify(viewState, never()).restoreItem(model, 0)
    }

    @Test
    fun `remove favorite translation error`() {
        val model = getFavoritesTranslations().first()
        whenever(interactor.removeFavoriteTranslation(model))
            .thenReturn(Completable.error(IOException()))

        presenter.removeFavoriteTranslation(model, 0)

        verify(viewState, never()).showItemDeletedMessage()
        verify(viewState).restoreItem(model, 0)
    }

    private fun getFavoritesTranslations(): List<FavoriteTranslationModel> {
        return listOf(
            FavoriteTranslationModel(
                1,
                "original1",
                "translation1",
                11
            ),
            FavoriteTranslationModel(
                2,
                "original2",
                "translation2",
                12
            ),
            FavoriteTranslationModel(
                3,
                "original3",
                "translation3",
                13
            )
        )
    }
}