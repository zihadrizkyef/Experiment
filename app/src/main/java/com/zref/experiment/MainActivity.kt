package com.zref.experiment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.zref.experiment.databinding.ActivityMainBinding
import io.realm.*
import io.realm.annotations.PrimaryKey
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
        fetchData()
        clearData()
    }

    private fun initRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .name("zref-experiment-realm")
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()

        realm = Realm.getInstance(config)
    }

    private fun insertData() {
        realm.executeTransaction {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, 2022)
            calendar.set(Calendar.MONTH, 10)
            calendar.set(Calendar.DAY_OF_MONTH, 10)
            it.insertOrUpdate(Task().apply { name = "Lamborghini"; date = calendar.time })

            calendar.set(Calendar.DAY_OF_MONTH, 15)
            it.insertOrUpdate(Task().apply { name = "Ferari"; date = calendar.time })

            calendar.set(Calendar.DAY_OF_MONTH, 17)
            it.insertOrUpdate(Task().apply { name = "Becak"; date = calendar.time })

            calendar.set(Calendar.DAY_OF_MONTH, 20)
            it.insertOrUpdate(Task().apply { name = "Angkot"; date = calendar.time })
        }
    }

    private fun fetchData() {
        val list = realm.where<Task>().findAll()
        val copy = realm.copyFromRealm(list)
        Log.e("AOEU", "full fetch")
        Log.e("AOEU", GsonBuilder().setPrettyPrinting().create().toJson(copy))

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2022)
        calendar.set(Calendar.MONTH, 10)
        calendar.set(Calendar.DAY_OF_MONTH, 14)
        val date1 = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 18)
        val date2 = calendar.time
        val list2 = realm.where<Task>()
            .between("date", date1, date2)
            .findAll()
        val copy2 = realm.copyFromRealm(list2)
        Log.e("AOEU", "filter fetch")
        Log.e("AOEU", GsonBuilder().setPrettyPrinting().create().toJson(copy2))
    }

    private fun clearData() {
        realm.executeTransaction {
            it.delete(Task::class.java)
        }
    }
}

open class Task : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var name = ""
    var date = Date()

}