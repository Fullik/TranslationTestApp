package com.example.translation.data.repository.translation

import com.example.translation.data.database.TranslationDao
import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.data.network.TranslationApi
import com.example.translation.data.network.response.TranslationResponse
import com.example.translation.domain.SupportedTranslations
import com.example.translation.getEntities
import com.example.translation.util.SchedulerProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TranslationRepositoryTest {
    private lateinit var translationDao: TranslationDao
    private lateinit var translationApi: TranslationApi
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var repository: TranslationRepositoryImpl

    @Before
    fun setup() {
        translationDao = mock()
        translationApi = mock()
        schedulerProvider = mock()
        whenever(schedulerProvider.getDatabaseScheduler()).thenReturn(Schedulers.trampoline())
        repository = TranslationRepositoryImpl(
            translationDao,
            translationApi,
            schedulerProvider
        )
    }

    @Test
    fun `translate word success`() {
        whenever(
            translationApi.translate(
                "someKek",
                SupportedTranslations.ENG.key,
                SupportedTranslations.RU.key
            )
        ).thenReturn(Single.just(TranslationResponse("someTranslation")))

        repository.translateWord("someKek", SupportedTranslations.RU, SupportedTranslations.ENG)
            .test()
            .assertValue {
                it.translation == "someTranslation"
            }
    }

    @Test
    fun `translate word error`() {
        whenever(
            translationApi.translate(
                "someKek",
                SupportedTranslations.ENG.key,
                SupportedTranslations.RU.key
            )
        ).thenReturn(Single.error(IOException()))

        repository.translateWord("someKek", SupportedTranslations.RU, SupportedTranslations.ENG)
            .test()
            .assertError(IOException::class.java)
    }

    @Test
    fun `observe translation changes success`() {
        whenever(translationDao.getAllTranslations())
            .thenReturn(Flowable.just(getEntities()))

        repository.observeTranslationChanges()
            .test()
            .assertValue { it.size == 3 }
            .assertNoErrors()
    }

    @Test
    fun `observe translation changes error`() {
        whenever(translationDao.getAllTranslations())
            .thenReturn(Flowable.error(IOException()))

        repository.observeTranslationChanges()
            .test()
            .assertError(IOException::class.java)
    }

    @Test
    fun `save translation while no same translation in db`() {
        whenever(
            translationDao.searchTranslation(
                "someKek",
                "someTranslation"
            )
        ).thenReturn(null)

        repository.saveTranslation(
            "someKek",
            "someTranslation",
            SupportedTranslations.RU,
            SupportedTranslations.ENG
        ).test().assertComplete()

        val captor = argumentCaptor<TranslationEntity>()
        verify(translationDao).insertTranslation(captor.capture())
        assert(
            captor.firstValue.run {
                originalWord == "someKek" &&
                        wordTranslation == "someTranslation" &&
                        translatedFrom == "ru" &&
                        translatedTo == "en"
            }
        )
    }

    @Test
    fun `do not insert same already existed translation into db`() {
        whenever(
            translationDao.searchTranslation(
                "someKek",
                "someTranslation"
            )
        ).thenReturn(
            TranslationEntity(
                1,
                "someKek",
                "someTranslation",
                "ru",
                "en",
                12
            )
        )

        repository.saveTranslation(
            "someKek",
            "someTranslation",
            SupportedTranslations.RU,
            SupportedTranslations.ENG
        ).test().assertComplete()

        verify(translationDao, never()).insertTranslation(any())
    }

    @Test
    fun `search word success`() {
        whenever(translationDao.searchWord("original"))
            .thenReturn(Single.just(getEntities()))

        repository.searchWord("original")
            .test()
            .assertValue { it.size == 3 }
            .assertNoErrors()
    }

    @Test
    fun `search word success error`() {
        whenever(translationDao.searchWord("original"))
            .thenReturn(Single.error(IOException()))

        repository.searchWord("original")
            .test()
            .assertError(IOException::class.java)
    }
}