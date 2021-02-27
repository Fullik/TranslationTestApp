package com.example.translation.presentation.view.translation

import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface TranslationView : MvpView {
    @OneExecution
    fun showTranslationError()
    @OneExecution
    fun showWordEmptyError()
    @OneExecution
    fun showSearchError()
    @AddToEndSingle
    fun setupSpinners(availableLanguages: List<AvailableLanguagesModel>)
    @AddToEndSingle
    fun showRecentTranslations(items: List<RecentTranslationModel>)
}