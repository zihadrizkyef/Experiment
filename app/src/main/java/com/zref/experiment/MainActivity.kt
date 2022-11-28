package com.zref.experiment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zref.experiment.databinding.ActivityMainBinding
import io.realm.*
import io.realm.kotlin.where
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRealm()
        insertData()
        getData()
    }

    private fun initRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .encryptionKey("2708198190123456789012345678901234567890123456789012345610102015".toByteArray())
            .schemaVersion(2L)
            .migration(CustomRealmMigration())
            .allowWritesOnUiThread(true)
            .build()

        realm = Realm.getInstance(config)
    }

    private fun insertData() {
        if (realm.where<User>().findAll().isEmpty()) {
            Log.i("AOEU", "inserting data")
            realm.executeTransaction {
                it.insert(
                    listOf(
                        User().apply {
                            name = "Zihad"
                            age = 20
                            money = 10
                        },
                        User().apply {
                            name = "Udin"
                            age = 15
                            money = 13
                        },
                        User().apply {
                            name = "Budi"
                            age = 11
                            money = 50
                        },
                        User().apply {
                            name = "Marno"
                            age = 57
                            money = 75
                        },
                    )
                )
            }
        }
    }

    private fun getData() {
        val list = realm.where<User>().findAll()
        realm.copyFromRealm(list)
        Log.i("AOEU", "getting data")
        list.forEach {
            Log.i("AOEU", "${it.name} ${it.age} ${it.money}")
        }
    }
}