package com.example.translation.presentation.view.translation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.translation.R
import com.example.translation.domain.model.RecentTranslationModel

class TranslationAdapter(
    private val favoriteButtonListener: FavoriteButtonListener
) : ListAdapter<RecentTranslationModel, TranslationViewHolder>(
    TranslateDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recent_translation, parent, false)
        return TranslationViewHolder(view, favoriteButtonListener)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}