package com.example.translation.util

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun getDatabaseScheduler() : Scheduler
}