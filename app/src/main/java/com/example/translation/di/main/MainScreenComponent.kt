package com.example.translation.di.main

import com.example.translation.di.translation.TranslationFragmentComponent
import com.example.translation.presentation.view.root.MainActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [MainScreenModule::class]
)
interface MainScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): MainScreenComponent
    }

    fun translationFragmentComponent(): TranslationFragmentComponent.Builder

    fun inject(mainActivity: MainActivity)
}