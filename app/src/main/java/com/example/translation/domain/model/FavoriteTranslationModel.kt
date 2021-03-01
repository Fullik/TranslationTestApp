package com.example.translation.domain.model

data class FavoriteTranslationModel(
    val id: Long,
    val originalWord: String,
    val translation: String,
    val timestamp: Long
)