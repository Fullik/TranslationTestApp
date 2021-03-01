package com.example.translation.presentation.view.translation

import com.example.translation.domain.model.RecentTranslationModel

interface FavoriteButtonListener {
    fun onFavoriteButtonClicked(model: RecentTranslationModel, position: Int)
}