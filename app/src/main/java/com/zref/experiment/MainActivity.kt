package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countDownTimer = object : CountDownTimer((1000*60*60*3)+(1000*90), 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val hours = (millisUntilFinished / (1000 * 60 * 60) % 24)
                binding.textView.text = "($hours:$minutes:$seconds)"
            }

            override fun onFinish() {
                binding.textView.text = "DONE"
            }
        }.start()

        binding.textView.setOnClickListener {
            countDownTimer.cancel()
        }
    }
}