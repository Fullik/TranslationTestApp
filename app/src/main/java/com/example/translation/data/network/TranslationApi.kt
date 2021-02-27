package com.example.translation.data.network

import com.example.translation.data.network.response.TranslationResponse
import io.reactivex.Single
import retrofit2.http.*

interface TranslationApi {
    @GET("exec")
    fun translate(
        @Query("word") word: String,
        @Query("target") languageTarget: String,
        @Query("source") languageSource: String
    ): Single<TranslationResponse>
}