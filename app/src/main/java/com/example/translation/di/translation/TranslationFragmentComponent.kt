package com.example.translation.di.translation

import com.example.translation.presentation.presenter.translation.TranslationPresenter
import com.example.translation.presentation.view.translation.TranslationFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [TranslationFragmentModule::class]
)
interface TranslationFragmentComponent {

    fun translationPresenter(): TranslationPresenter

    @Subcomponent.Builder
    interface Builder {
        fun build(): TranslationFragmentComponent
    }

    fun inject(fragment: TranslationFragment)
}