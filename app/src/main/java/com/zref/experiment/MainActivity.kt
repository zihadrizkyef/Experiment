package com.zref.experiment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zref.experiment.databinding.ActivityMainBinding
import io.realm.mongodb.AppException
import io.realm.query
import kotlinx.coroutines.*

    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        private val realm by lazy { (application as CustomApplication).realm }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            realm.writeBlocking {
                copyToRealm(Buku(
                    name = "Perjuangan Menjual Baju"
                ))
                copyToRealm(Buku(
                    name = "Perang Saudara"
                ))
            }

            val buku = realm.query<Buku>("name BEGINSWITH $0", "pera")
            Log.i("AOEU", "buku = $buku")

            CoroutineScope(Dispatchers.Main).launch {
                val query = realm.query<Buku>().find()
                realm.write {
                    delete(query)
                }
            }
        }
    }