package com.example.translation

import com.example.translation.data.database.entity.TranslationEntity

fun getEntities() = listOf(
    TranslationEntity(
        1,
        "original1",
        "translation1",
        "ru",
        "en",
        10
    ),
    TranslationEntity(
        2,
        "original2",
        "translation2",
        "en",
        "ru",
        12
    ),
    TranslationEntity(
        3,
        "original3",
        "translation3",
        "de",
        "ru",
        11
    )
)