package com.example.translation.presentation.presenter.translation

import android.util.Log
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import com.example.translation.domain.translation.TranslationInteractor
import com.example.translation.presentation.presenter.base.BasePresenter
import com.example.translation.presentation.view.translation.FavoriteButtonListener
import com.example.translation.presentation.view.translation.TranslationView
import com.github.terrakok.cicerone.Router
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class TranslationPresenter(
    private val interactor: TranslationInteractor,
    private val router: Router
) : BasePresenter<TranslationView>(), FavoriteButtonListener {

    private var searchDisposable: Disposable? = null

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
        interactor.searchWord(word)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewState.showRecentTranslations(it) }, { viewState.showSearchError() })
            .disposeOnDestroy()
    }

    override fun onFavoriteButtonClicked(model: RecentTranslationModel, position: Int) {
        interactor.changeFavoriteState(model)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { /*Do nothing*/ },
                { viewState.showFavoriteStateChangeError() }
            )
            .disposeOnDestroy()
    }

    fun startObserveSearchInputChanges(textChanges: Observable<CharSequence>) {
        searchDisposable = textChanges
            .toFlowable(BackpressureStrategy.LATEST)
            .debounce(SEARCH_INPUT_DEBOUNCE_MS, TimeUnit.MILLISECONDS)
            .skip(1)
            .map { it.toString() }
            .switchMapSingle { interactor.searchWord(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewState.showRecentTranslations(it) }, { viewState.showSearchError() })
    }

    fun stopObserveSearchInputChanges() {
        searchDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopObserveSearchInputChanges()
    }

    private companion object {
        private const val SEARCH_INPUT_DEBOUNCE_MS = 500L
    }
}