package com.example.translation.domain

import android.content.Context
import androidx.annotation.StringRes

class ResourceManagerImpl(private val context: Context) : ResourceManager {

    override fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }
}