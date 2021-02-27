package com.example.translation.domain

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringRes: Int): String
}