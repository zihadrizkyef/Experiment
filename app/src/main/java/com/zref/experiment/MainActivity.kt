package com.zref.experiment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.zref.experiment.databinding.ActivityMainBinding
import io.realm.*
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import org.bson.types.ObjectId
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
        deleteAll()
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
            it.insertOrUpdate(Car().apply { name = "Lamborghini"; user = realmListOf(User().apply { name = "Zihad" }) })
            it.insertOrUpdate(Car().apply { name = "Ferari"; user = realmListOf(User().apply { name = "Udin" }, User().apply { name = "Asep" }, User().apply { name = "Fuad" }) })
            it.insertOrUpdate(Car().apply { name = "Angkot"; user = realmListOf(User().apply { name = "Marno" }) })
        }
    }

    private fun fetchData() {
        val list = realm.where<Car>().equalTo("user.name", "asep", Case.INSENSITIVE).findAll()
        val copy = realm.copyFromRealm(list.first())
        Log.e("AOEU", GsonBuilder().setPrettyPrinting().create().toJson(copy))
    }

    private fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }
}

open class Car : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var name = ""
    var user = realmListOf<User>()

}

open class User : RealmObject() {

    @PrimaryKey
    var name = ""

}

fun <T> realmListOf(vararg objects: T): RealmList<T> {
    val list = RealmList<T>()
    list.addAll(objects)
    return list
}