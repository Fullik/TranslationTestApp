package com.example.translation.data.network.response

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("translation")
    val translation: String
)