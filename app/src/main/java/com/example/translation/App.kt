package com.example.translation

import android.app.Application
import com.example.translation.di.ComponentHolder

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ComponentHolder.INSTANCE.initAppComponent(this)
    }
}