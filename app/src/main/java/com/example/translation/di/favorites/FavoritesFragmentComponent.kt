package com.example.translation.di.favorites

import com.example.translation.presentation.view.favorites.FavoritesFragment
import dagger.Subcomponent

@Subcomponent(modules = [FavoritesFragmentModule::class])
interface FavoritesFragmentComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): FavoritesFragmentComponent
    }

    fun inject(fragment: FavoritesFragment)
}