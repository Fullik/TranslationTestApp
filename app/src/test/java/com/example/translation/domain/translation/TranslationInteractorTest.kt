package com.example.translation.domain.translation

import com.example.translation.data.network.response.TranslationResponse
import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.ResourceManager
import com.example.translation.domain.SupportedTranslations
import com.example.translation.domain.model.RecentTranslationModel
import com.example.translation.getEntities
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TranslationInteractorTest {

    private lateinit var interactor: TranslationInteractorImpl
    private lateinit var repository: TranslationRepository
    private lateinit var resourceManager: ResourceManager

    @Before
    fun setup() {
        repository = mock()
        resourceManager = mock()
        interactor = TranslationInteractorImpl(repository, resourceManager)
    }

    @Test
    fun `get available languages`() {
        whenever(resourceManager.getString(any())).thenReturn("kek")
        val result = interactor.getAvailableLanguages()
        assertTrue(result.size == 3)
        result.forEach {
            assert(it.name == "kek")
            assertTrue(SupportedTranslations.values().contains(it.languageType))
        }
    }

    @Test
    fun `translate word success`() {
        whenever(
            repository.translateWord(
                "someKek",
                SupportedTranslations.ENG,
                SupportedTranslations.RU
            )
        ).thenReturn(Single.just(TranslationResponse("someKekTranslation")))
        whenever(
            repository.saveTranslation(
                "someKek",
                "someKekTranslation",
                SupportedTranslations.ENG,
                SupportedTranslations.RU
            )
        ).thenReturn(Completable.complete())

        interactor.translateWord("someKek", SupportedTranslations.ENG, SupportedTranslations.RU)
            .test()
            .assertComplete()

        verify(repository).translateWord(
            "someKek",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )
        verify(repository).saveTranslation(
            "someKek",
            "someKekTranslation",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )
    }

    @Test
    fun `translate word request error`() {
        whenever(
            repository.translateWord(
                "someKek",
                SupportedTranslations.ENG,
                SupportedTranslations.RU
            )
        ).thenReturn(Single.error(IOException()))

        interactor.translateWord("someKek", SupportedTranslations.ENG, SupportedTranslations.RU)
            .test()
            .assertError(IOException::class.java)

        verify(repository).translateWord(
            "someKek",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )

        verify(repository, never()).saveTranslation(
            "someKek",
            "someKekTranslation",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )
    }

    @Test
    fun `save translation error`() {
        whenever(
            repository.translateWord(
                "someKek",
                SupportedTranslations.ENG,
                SupportedTranslations.RU
            )
        ).thenReturn(Single.just(TranslationResponse("someKekTranslation")))
        whenever(
            repository.saveTranslation(
                "someKek",
                "someKekTranslation",
                SupportedTranslations.ENG,
                SupportedTranslations.RU
            )
        ).thenReturn(Completable.error(IOException()))

        interactor.translateWord("someKek", SupportedTranslations.ENG, SupportedTranslations.RU)
            .test()
            .assertError(IOException::class.java)

        verify(repository).translateWord(
            "someKek",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )

        verify(repository).saveTranslation(
            "someKek",
            "someKekTranslation",
            SupportedTranslations.ENG,
            SupportedTranslations.RU
        )
    }

    @Test
    fun `get recent translations success`() {
        whenever(repository.observeTranslationChanges())
            .thenReturn(Flowable.just(getEntities()))

        interactor.getRecentTranslations()
            .test()
            .assertValue { it.checkTestValues() }
    }

    @Test
    fun `get recent translation error`() {
        whenever(repository.observeTranslationChanges())
            .thenReturn(Flowable.error(IOException()))

        interactor.getRecentTranslations()
            .test()
            .assertError(IOException::class.java)
    }

    @Test
    fun `search word success`() {
        whenever(repository.searchWord("some"))
            .thenReturn(Single.just(getEntities()))

        interactor.searchWord("some")
            .test()
            .assertValue { it.checkTestValues() }

        verify(repository, never()).getAllTranslations()
    }

    @Test
    fun `search word error`() {
        whenever(repository.searchWord("some"))
            .thenReturn(Single.error(IOException()))

        interactor.searchWord("some")
            .test()
            .assertError(IOException::class.java)

        verify(repository, never()).getAllTranslations()
    }

    @Test
    fun `search by empty word`() {
        whenever(repository.getAllTranslations())
            .thenReturn(Single.just(getEntities()))

        interactor.searchWord("")
            .test()
            .assertValue { it.checkTestValues() }

        verify(repository, never()).searchWord("")
    }

    @Test
    fun `change favorite state success`() {
        val model = RecentTranslationModel(
            1,
            "originalWord",
            "translation",
            11,
            false
        )
        whenever(repository.changeTranslationFavoriteState(model.id, true))
            .thenReturn(Completable.complete())

        interactor.changeFavoriteState(model)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `change favorite state error`() {
        val model = RecentTranslationModel(
            1,
            "originalWord",
            "translation",
            11,
            false
        )

        whenever(repository.changeTranslationFavoriteState(model.id, true))
            .thenReturn(Completable.error(IOException()))

        interactor.changeFavoriteState(model)
            .test()
            .assertError(IOException::class.java)
    }

    private fun List<RecentTranslationModel>.checkTestValues(): Boolean {
        val isSorted = asSequence()
            .zipWithNext { a, b -> a.timestamp > b.timestamp }
            .all { it }
        return size == 3 && isSorted
    }
}