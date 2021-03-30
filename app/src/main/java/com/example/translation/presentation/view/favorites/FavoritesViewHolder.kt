package com.example.translation.presentation.view.favorites

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.domain.model.FavoriteTranslationModel

class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val originalWord = itemView.findViewById<TextView>(R.id.original_word)
    private val translation = itemView.findViewById<TextView>(R.id.translation)
    private val favoriteButton = itemView.findViewById<ImageButton>(R.id.favorite_button)
    lateinit var model: FavoriteTranslationModel

    fun bind(model: FavoriteTranslationModel) {
        this.model = model
        originalWord.text = model.originalWord
        translation.text = model.translation
        favoriteButton.visibility = View.GONE
    }
}