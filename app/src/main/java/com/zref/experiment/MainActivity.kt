package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text1.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("AOEU", "text1 ${binding.text1.width} -> ${binding.text2.width}")
        }

        binding.text2.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("AOEU", "text2 ${binding.text2.width} -> ${binding.text1.width}")
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("AOEU", "root ${binding.text1.width} -> ${binding.text2.width}")
        }
    }
}