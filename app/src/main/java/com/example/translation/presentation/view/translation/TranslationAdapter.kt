package com.example.translation.presentation.view.translation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.domain.model.RecentTranslationModel

class TranslationAdapter : RecyclerView.Adapter<TranslationViewHolder>() {
    private val items = mutableListOf<RecentTranslationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recent_translation, parent, false)
        return TranslationViewHolder(view)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<RecentTranslationModel>) {
        val diffCallback = TranslateDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.run {
            clear()
            addAll(items)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}