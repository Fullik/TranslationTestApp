package com.example.translation.presentation.view.translation

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.domain.model.RecentTranslationModel

class TranslationViewHolder(
    itemView: View,
    private val recentTranslationListener: FavoriteButtonListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val originalWord = itemView.findViewById<TextView>(R.id.original_word)
    private val translation = itemView.findViewById<TextView>(R.id.translation)
    private val favoriteButton = itemView.findViewById<ImageButton>(R.id.favorite_button)
    private lateinit var model: RecentTranslationModel

    fun bind(model: RecentTranslationModel) {
        this.model = model
        originalWord.text = model.originalWord
        translation.text = model.translation
        favoriteButton.setImageResource(
            if (model.isFavorite) R.drawable.ic_baseline_star_24
            else R.drawable.ic_baseline_star_outline_24
        )
        favoriteButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (::model.isInitialized) {
            recentTranslationListener.onFavoriteButtonClicked(model, adapterPosition)
        }
    }
}