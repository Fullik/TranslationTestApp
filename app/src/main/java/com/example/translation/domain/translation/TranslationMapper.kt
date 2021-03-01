package com.example.translation.domain.translation

import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.domain.model.RecentTranslationModel

object TranslationMapper {
    fun mapFromEntity(entity: TranslationEntity): RecentTranslationModel {
        return RecentTranslationModel(
            entity.id,
            entity.originalWord,
            entity.wordTranslation,
            entity.timestamp,
            entity.isFavorite
        )
    }
}