package com.zref.experiment

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.log.LogLevel

class CustomApplication: Application() {
    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
    }
}