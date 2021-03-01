package com.example.translation.di

import com.example.translation.App
import com.example.translation.di.favorites.FavoritesFragmentComponent
import com.example.translation.di.main.MainScreenComponent
import com.example.translation.di.translation.TranslationFragmentComponent

class ComponentHolder private constructor() {

    lateinit var appComponent: AppComponent
        private set

    private var mainScreenComponent: MainScreenComponent? = null
    private var translationScreenComponent: TranslationFragmentComponent? = null
    private var favoritesFragmentComponent: FavoritesFragmentComponent? = null

    fun initAppComponent(app: App) {
        appComponent = DaggerAppComponent.builder()
            .application(app)
            .build()
    }

    fun getMainScreenComponent(): MainScreenComponent {
        return mainScreenComponent
            ?: appComponent.mainScreenComponent()
                .build()
                .apply { mainScreenComponent = this }
    }

    fun getTranslationScreenComponent(): TranslationFragmentComponent {
        return translationScreenComponent
            ?: getMainScreenComponent()
                .translationFragmentComponent()
                .build()
                .apply { translationScreenComponent = this }
    }

    fun getFavoritesFragmentComponent(): FavoritesFragmentComponent {
        return favoritesFragmentComponent
            ?: getMainScreenComponent()
                .favoritesFragmentComponent()
                .build()
                .apply { favoritesFragmentComponent = this }
    }

    fun clearFavoritesFragmentComponent() {
        favoritesFragmentComponent = null
    }

    fun clearTranslationScreenComponent() {
        translationScreenComponent = null
    }

    fun clearMainScreenComponent() {
        mainScreenComponent = null
    }

    companion object {
        val INSTANCE: ComponentHolder = ComponentHolder()
    }
}