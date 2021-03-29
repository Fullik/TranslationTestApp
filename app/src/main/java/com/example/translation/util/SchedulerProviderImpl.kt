package com.example.translation.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulerProviderImpl : SchedulerProvider {

    override fun getDatabaseScheduler(): Scheduler = Schedulers.io()
}