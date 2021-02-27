package com.example.translation.domain.model

import com.example.translation.domain.SupportedTranslations

data class AvailableLanguagesModel(
    val name: String,
    val languageType: SupportedTranslations
)