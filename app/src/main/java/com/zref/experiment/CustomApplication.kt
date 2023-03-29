package com.zref.experiment

import android.app.Application
import timber.log.Timber

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}