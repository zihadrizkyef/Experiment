package com.zref.experiment

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@CustomApplication)
            modules(appModules)
        }
    }
}