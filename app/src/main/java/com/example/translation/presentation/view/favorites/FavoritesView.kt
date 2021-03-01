package com.example.translation.presentation.view.favorites

import com.example.translation.domain.model.FavoriteTranslationModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface FavoritesView : MvpView {
    @OneExecution
    fun showFavoriteTranslationsFetchError()
    @AddToEndSingle
    fun showFavorites(items: List<FavoriteTranslationModel>)
    @OneExecution
    fun restoreItem(model: FavoriteTranslationModel, position: Int)
    @OneExecution
    fun showItemDeletedMessage()
}