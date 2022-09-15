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
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.types.RealmList
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
            .Builder(schema = setOf(Book::class, User::class))
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        val realm = Realm.open(configuration = config)

        realm.writeBlocking {
            copyToRealm(Book().apply {
                id = "0"
                name = "Book Green"
                user = realmListOf(
                    User().apply { id="0" ; name = "zihad" },
                    User().apply { id="1" ; name = "marno" }
                )
            }, UpdatePolicy.ALL)
            copyToRealm(Book().apply {
                id = "1"
                name = "Book Blue"
                user = realmListOf(
                    User().apply { id="5" ; name = "budi" },
                    User().apply { id="6" ; name = "bayan" }
                )
            }, UpdatePolicy.ALL)
        }

        realm.query<Book>().find().forEach {
            Log.i("AOEU", it.toString())
        }

        realm.query<Book>("user.id = $0", "5").find().forEach {
            Log.i("AOEU", "OKEH $it")
        }

        realm.writeBlocking {
            val query = this.query<Book>().find()
            this.delete(query)
        }
    }
}

class Book : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var name: String = ""
    var user: RealmList<User> = realmListOf()

    override fun toString(): String {
        return "Book(id='$id', name='$name', user=$user)"
    }
}

class User : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var name = ""

    override fun toString(): String {
        return "User(id='$id', name='$name')"
    }
}