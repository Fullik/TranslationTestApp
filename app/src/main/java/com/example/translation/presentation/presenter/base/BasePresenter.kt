package com.example.translation.presentation.presenter.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<T : MvpView> : MvpPresenter<T>() {
    private val onDestroyDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.clear()
    }

    protected fun Disposable.disposeOnDestroy() {
        onDestroyDisposable.add(this)
    }
}