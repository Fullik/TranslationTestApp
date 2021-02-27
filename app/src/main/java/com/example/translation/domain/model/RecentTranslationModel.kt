package com.example.translation.domain.model

data class RecentTranslationModel(
    val id: Long,
    val originalWord: String,
    val translation: String,
    val timestamp: Long
)