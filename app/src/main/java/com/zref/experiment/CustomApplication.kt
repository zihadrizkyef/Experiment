package com.zref.experiment

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel

class CustomApplication: Application() {
    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()

        val config = RealmConfiguration
            .Builder(schema = setOf(Buku::class))
            .name("bukuDB.db")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .log(LogLevel.ALL)
            .build()
        realm = Realm.open(configuration = config)
    }
}