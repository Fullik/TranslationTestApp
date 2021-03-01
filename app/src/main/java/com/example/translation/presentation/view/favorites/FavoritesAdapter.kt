package com.example.translation.presentation.view.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.domain.model.FavoriteTranslationModel

class FavoritesAdapter : RecyclerView.Adapter<FavoritesViewHolder>() {
    private val items = mutableListOf<FavoriteTranslationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recent_translation, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<FavoriteTranslationModel>) {
        this.items.run {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    fun remoteItemAt(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(model: FavoriteTranslationModel, position: Int) {
        this.items.add(position, model)
        notifyItemInserted(position)
    }
}