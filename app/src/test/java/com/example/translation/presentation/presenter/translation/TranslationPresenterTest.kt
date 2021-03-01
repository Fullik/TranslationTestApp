package com.example.translation.presentation.presenter.translation

import com.example.translation.domain.SupportedTranslations
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import com.example.translation.domain.translation.TranslationInteractor
import com.example.translation.presentation.view.translation.TranslationView
import com.example.translation.presentation.view.translation.`TranslationView$$State`
import com.github.terrakok.cicerone.Router
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TranslationPresenterTest {

    private lateinit var interactor: TranslationInteractor
    private lateinit var router: Router
    private lateinit var viewState: `TranslationView$$State`
    private lateinit var presenter: TranslationPresenter

    @Before
    fun setup() {
        interactor = mock()
        router = mock()
        viewState = mock()
        presenter = TranslationPresenter(interactor, router)
        presenter.setViewState(viewState)
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        val trampoline = TrampolineScheduler.instance()
        RxJavaPlugins.setComputationSchedulerHandler { trampoline }
        RxJavaPlugins.setInitComputationSchedulerHandler { trampoline }
        RxAndroidPlugins.setMainThreadSchedulerHandler { trampoline }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampoline }
    }

    @Test
    fun `setup view on first view attach`() {
        val view = mock<TranslationView>()
        val availableLanguages = listOf(AvailableLanguagesModel("English", SupportedTranslations.ENG))
        val recentTranslations = getRecentTranslations()
        whenever(interactor.getRecentTranslations())
            .thenReturn(Flowable.just(recentTranslations))
        whenever(interactor.getAvailableLanguages())
            .thenReturn(availableLanguages)

        presenter.attachView(view)
        verify(viewState).setupSpinners(availableLanguages)
        verify(viewState).showRecentTranslations(recentTranslations)
    }

    @Test
    fun `exit when tap on back button`() {
        presenter.onBackPressed()
        verify(router).exit()
    }

    @Test
    fun `do not translate empty word`() {
        presenter.translate(
            "",
            AvailableLanguagesModel("English", SupportedTranslations.ENG),
            AvailableLanguagesModel("Russian", SupportedTranslations.RU)
        )
        verify(viewState).showWordEmptyError()
        verify(interactor, never()).translateWord(any(), any(), any())
    }

    @Test
    fun `translate succeeded`() {
        whenever(interactor.translateWord("someWord", SupportedTranslations.ENG, SupportedTranslations.RU))
            .thenReturn(Completable.complete())

        presenter.translate(
            "someWord",
            AvailableLanguagesModel("English", SupportedTranslations.ENG),
            AvailableLanguagesModel("Russian", SupportedTranslations.RU)
        )

        verify(viewState, never()).showWordEmptyError()
        verify(viewState, never()).showTranslationError()
    }

    @Test
    fun `translate error`() {
        whenever(interactor.translateWord("someWord", SupportedTranslations.ENG, SupportedTranslations.RU))
            .thenReturn(Completable.error(IOException()))

        presenter.translate(
            "someWord",
            AvailableLanguagesModel("English", SupportedTranslations.ENG),
            AvailableLanguagesModel("Russian", SupportedTranslations.RU)
        )

        verify(viewState, never()).showWordEmptyError()
        verify(viewState).showTranslationError()
    }

    @Test
    fun `search word succeeded`() {
        val recentTranslations = getRecentTranslations()
        whenever(interactor.searchWord("originalWord"))
            .thenReturn(Single.just(recentTranslations))

        presenter.search("originalWord")

        verify(viewState).showRecentTranslations(recentTranslations)
        verify(viewState, never()).showSearchError()
    }

    @Test
    fun `search word error`() {
        whenever(interactor.searchWord("originalWord"))
            .thenReturn(Single.error(IOException()))

        presenter.search("originalWord")

        verify(viewState, never()).showRecentTranslations(any())
        verify(viewState).showSearchError()
    }

    @Test
    fun `search via input changes success`() {
        val inputSource = Observable.just<CharSequence>("", "s", "so", "som", "some")

        val emptyList = emptyList<RecentTranslationModel>()
        val recentTranslations = getRecentTranslations()
        whenever(interactor.searchWord(any()))
            .thenReturn(Single.just(emptyList))
        whenever(interactor.searchWord("some"))
            .thenReturn(Single.just(recentTranslations))

        presenter.startObserveSearchInputChanges(inputSource)

        verify(viewState, never()).showSearchError()
        verify(interactor, never()).searchWord("")
        verify(viewState, times(3)).showRecentTranslations(emptyList)
        verify(viewState).showRecentTranslations(recentTranslations)
    }

    @Test
    fun `error while searching word via input changes`() {
        val inputSource = Observable.just<CharSequence>("", "s", "so", "som", "some")

        val emptyList = emptyList<RecentTranslationModel>()
        whenever(interactor.searchWord(any()))
            .thenReturn(Single.just(emptyList))
        whenever(interactor.searchWord("some"))
            .thenReturn(Single.error(IOException()))

        presenter.startObserveSearchInputChanges(inputSource)

        verify(viewState).showSearchError()
        verify(interactor, never()).searchWord("")
        verify(viewState, times(3)).showRecentTranslations(emptyList)
    }

    private fun getRecentTranslations() = listOf(
        RecentTranslationModel(
            1,
            "originalWord1",
            "translationWord1",
            11,
            false
        ),
        RecentTranslationModel(
            2,
            "originalWord2",
            "translationWord2",
            12,
            false
        )
    )
}