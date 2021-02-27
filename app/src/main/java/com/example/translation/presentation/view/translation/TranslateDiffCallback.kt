package com.example.translation.presentation.view.translation

import androidx.recyclerview.widget.DiffUtil
import com.example.translation.domain.model.RecentTranslationModel

class TranslateDiffCallback : DiffUtil.ItemCallback<RecentTranslationModel>() {

    override fun areItemsTheSame(
        oldItem: RecentTranslationModel,
        newItem: RecentTranslationModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: RecentTranslationModel,
        newItem: RecentTranslationModel
    ): Boolean = oldItem == newItem

}