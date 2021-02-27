package com.example.translation.presentation.view.translation

import androidx.recyclerview.widget.DiffUtil
import com.example.translation.domain.model.RecentTranslationModel

class TranslateDiffCallback(
    private val oldList: List<RecentTranslationModel>,
    private val newList: List<RecentTranslationModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}