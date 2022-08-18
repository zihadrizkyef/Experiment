package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zref.experiment.databinding.ActivityMainBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewModelA().run()
    }

    class ViewModelA : ViewModel() {
        fun run() {
            viewModelScope.launch {
                val flow = flow {
                    for (i in 0 until 20) {
                        emit(i)
                    }
                }
                flow.collect {
                    when (it) {
                        10 -> {
                            Log.i("AOEU", "Okeh stop")
                            cancel()
                        }
                        else -> {
                            Log.i("AOEU", "$it Jangan stop")
                        }
                    }
                }

                Log.i("AOEU", "Apakah aku muncul?")
            }
        }
    }
}