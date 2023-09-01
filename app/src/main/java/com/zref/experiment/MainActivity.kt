package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zref.experiment.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

        viewModel.a.observe(this) {
            binding.textView.text = binding.textView.text.toString() + "\n" + it.toString()
        }

        viewModel.add1()
        viewModel.add2()
    }

    class ViewModelAnu : ViewModel() {
        val a = MutableLiveData<Int>()
        val mutex = Mutex()

        fun add1() {
            viewModelScope.launch {
                flow<Any> {
                    Thread.sleep(3000)
                    a.postValue(3000)
                }.flowOn(Dispatchers.IO).collect()
            }
        }

        fun add2() {
            viewModelScope.launch {
                mutex.withLock {
                    withContext(Dispatchers.IO) {
                        a.postValue(add2b())
                    }
                }
            }
        }

        private fun add2b(): Int {
            Thread.sleep(1000)
            return 1000
        }
    }
}