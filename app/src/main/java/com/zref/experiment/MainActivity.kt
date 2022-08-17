package com.zref.experiment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.zref.experiment.databinding.ActivityMainBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files.delete
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val config = RealmConfiguration
            .Builder(schema = setOf(Book::class))
            .schemaVersion(1)
            .build()
        val realm = Realm.open(configuration = config)

        realm.writeBlocking {
            copyToRealm(Book().apply {
                id = "0"
                name = "Book Green"
            }, UpdatePolicy.ALL)
            copyToRealm(Book().apply {
                id = "1"
                name = "Book Blue"
            }, UpdatePolicy.ALL)
        }

        Log.i("AOEU", "AFTER INPUT")
        val list1 = realm.query<Book>().find()
        list1.forEach {
            Log.i("AOEU", it.name)
        }

        realm.writeBlocking {
            val query = query<Book>("name CONTAINS[c] $0", "blue")
            delete(query)
        }

        Log.i("AOEU", "AFTER DELETE CONTAINS[c] BLUE")
        val list2 = realm.query<Book>().find()
        list2.forEach {
            Log.i("AOEU", it.name)
        }

        Log.i("AOEU", "CONTAINS BLUE")
        val list3 = realm.query<Book>("name CONTAINS[c] $0", "blue").find()
        list3.forEach {
            Log.i("AOEU", it.name)
        }
    }
}

    class Book : RealmObject {
        @PrimaryKey
        var id: String = UUID.randomUUID().toString()
        var name: String = ""
    }