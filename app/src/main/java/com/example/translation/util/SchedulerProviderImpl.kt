package com.example.translation.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProviderImpl : SchedulerProvider {

    override fun getDatabaseScheduler(): Scheduler = Schedulers.io()

    override fun getUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    override fun getTextDebounceScheduler(): Scheduler = Schedulers.computation()
}