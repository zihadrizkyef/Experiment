package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zref.experiment.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = ViewModelAnu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.text.observe(this) {
            binding.textView.text = it
        }

        viewModel.add1500()
        viewModel.add700()
    }

    class ViewModelAnu : ViewModel() {
        val repository = RepositoryAnu()
        val text = MutableLiveData("Hello World!")
        val mutex = Mutex()

        fun add1500() {
            viewModelScope.launch {
                println("ANU wkwkwk1")
                mutex.withLock {
                    println("ANU wkwkwk2")
                    text.value = repository.repoAdd1500(text.value!!)
                    println("ANU wkwkwk3")
                }
            }
        }

        fun add700() {
            viewModelScope.launch {
                println("ANU wkwkwk4")
                mutex.withLock {
                    println("ANU wkwkwk5")
                    text.value = repository.repoAdd700(text.value!!)
                    println("ANU wkwkwk6")
                }
            }
        }
    }

    class RepositoryAnu {
        suspend fun repoAdd1500(current: String): String {
            return withContext(Dispatchers.IO) {
                Thread.sleep(1500)
                "$current\n1500"
            }
        }

        suspend fun repoAdd700(current: String): String {
            return withContext(Dispatchers.IO) {
                Thread.sleep(700)
                "$current\n700"
            }
        }
    }
}