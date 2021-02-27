package com.example.translation.presentation.view.translation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.domain.model.RecentTranslationModel

class TranslationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val originalWord = itemView.findViewById<TextView>(R.id.original_word)
    private val translation = itemView.findViewById<TextView>(R.id.translation)
    private lateinit var model: RecentTranslationModel

    fun bind(model: RecentTranslationModel) {
        this.model = model
        originalWord.text = model.originalWord
        translation.text = model.translation
    }
}