package com.example.translation.presentation.presenter.translation

import android.util.Log
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.translation.TranslationInteractor
import com.example.translation.presentation.presenter.base.BasePresenter
import com.example.translation.presentation.view.translation.TranslationView
import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class TranslationPresenter @Inject constructor(
    private val interactor: TranslationInteractor,
    private val router: Router
) : BasePresenter<TranslationView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setupSpinners(interactor.getAvailableLanguages())
        interactor.getRecentTranslations()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.showRecentTranslations(it) },
                { Log.e("TestApp", "Error in translation flowable", it) }
            ).disposeOnDestroy()
    }

    fun onBackPressed() {
        router.exit()
    }

    fun translate(
        word: String,
        translateFrom: AvailableLanguagesModel,
        translateTo: AvailableLanguagesModel
    ) {
        if (word.isEmpty()) {
            viewState.showWordEmptyError()
        } else {
            interactor.translateWord(word, translateFrom.languageType, translateTo.languageType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ /*Do nothing*/ }, { viewState.showTranslationError() })
                .disposeOnDestroy()
        }
    }

    fun search(word: String) {
        if (word.isNotEmpty()) {
            interactor.searchWord(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showRecentTranslations(it) }, { viewState.showSearchError() })
                .disposeOnDestroy()
        }
    }
}