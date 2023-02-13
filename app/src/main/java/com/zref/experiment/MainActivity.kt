package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.zref.experiment.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            var timer = Timer()
            var taskTimer: TimerTask = object : TimerTask() {
                override fun run() {
                    Log.i("AOEU", "timer executed")
                }
            }
            timer.scheduleAtFixedRate(taskTimer, 5000L, 2000L)
        }
    }
}