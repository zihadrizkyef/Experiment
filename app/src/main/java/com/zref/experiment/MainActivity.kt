package com.zref.experiment

import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.zref.experiment.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customerDisplay: CustomerDisplayPresentation
    private val videoResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            customerDisplay.setVideoUri(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            videoResultCallback.launch("video/*")
        }

        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val secondDisplay = displayManager.displays[1]
        customerDisplay = CustomerDisplayPresentation(this, secondDisplay)
        customerDisplay.show()
    }
}