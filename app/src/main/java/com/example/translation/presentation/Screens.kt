package com.example.translation.presentation

import com.example.translation.presentation.view.translation.TranslationFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun translation() = FragmentScreen {
        TranslationFragment.newInstance()
    }
}