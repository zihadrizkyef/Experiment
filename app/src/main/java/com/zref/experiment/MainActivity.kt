package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zref.experiment.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = ViewModelAnu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1500.setOnClickListener {
            viewModel.add1500()
        }

        binding.button700.setOnClickListener {
            viewModel.add700()
        }

        viewModel.text.observe(this) {
            binding.textView.text = it
        }

        viewModel.isRunningProcess.observe(this) {
            println("STATUS PROGRESS LOADING: $it")
            binding.progress.isVisible = it
        }

        viewModel.add1500()
        viewModel.add700()
    }

    class ViewModelAnu : ViewModel() {
        val repository = RepositoryAnu()
        val text = MutableLiveData("Hello World!")
        val isRunningProcess = MutableLiveData(false)
        val mutex = Mutex()

        fun add1500() {
            viewModelScope.processSalesOrder {
                text.value = repository.repoAdd1500(text.value!!)
            }
        }

        fun add700() {
            viewModelScope.processSalesOrder {
                text.value = repository.repoAdd700(text.value!!)
            }
        }


        private fun CoroutineScope.processSalesOrder(
            action: suspend () -> Unit
        ) {
            launch {
                mutex.lock()

                try {
                    isRunningProcess.value = mutex.isLocked
                    action()
                } finally {
                    mutex.unlock()
                    isRunningProcess.value = mutex.isLocked
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