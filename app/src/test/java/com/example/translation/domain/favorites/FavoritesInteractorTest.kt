package com.example.translation.domain.favorites

import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.model.FavoriteTranslationModel
import com.example.translation.getEntities
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FavoritesInteractorTest {
    private lateinit var interactor: FavoritesInteractor
    private lateinit var repository: TranslationRepository

    @Before
    fun setup() {
        repository = mock()
        interactor = FavoritesInteractor(repository)
    }

    @Test
    fun `get all favorite translations success`() {
        val entities = getEntities(true)
        whenever(repository.getAllFavoriteTranslations())
            .thenReturn(Single.just(entities))

        interactor.getAllFavoriteTranslations()
            .test()
            .assertValue { it.checkTestValues() }
    }

    @Test
    fun `get all favorite translations error`() {
        whenever(repository.getAllFavoriteTranslations())
            .thenReturn(Single.error(IOException()))

        interactor.getAllFavoriteTranslations()
            .test()
            .assertError(IOException::class.java)
    }

    @Test
    fun `remove favorite translation success`() {
        val model = FavoriteTranslationModel(
            1,
            "original",
            "translation",
            11
        )
        whenever(repository.changeTranslationFavoriteState(model.id, false))
            .thenReturn(Completable.complete())

        interactor.removeFavoriteTranslation(model)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `remove favorite translation error`() {
        val model = FavoriteTranslationModel(
            1,
            "original",
            "translation",
            11
        )
        whenever(repository.changeTranslationFavoriteState(model.id, false))
            .thenReturn(Completable.error(IOException()))

        interactor.removeFavoriteTranslation(model)
            .test()
            .assertError(IOException::class.java)
    }

    private fun List<FavoriteTranslationModel>.checkTestValues(): Boolean {
        val isSorted = asSequence()
            .zipWithNext { a, b -> a.timestamp > b.timestamp }
            .all { it }
        return size == 3 && isSorted
    }
}