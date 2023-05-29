package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMerah.setOnClickListener {
            binding.textHelloAndre.isVisible = true
            isUp = !isUp
            if (isUp) {
                animUp()
            } else {
                animDown()
            }
        }
    }

    fun animUp() {
        binding.textHelloWorld.animate()
            .translationY(-300.dp)
            .alpha(0F)
            .start()
        binding.textHelloAndre.animate()
            .translationY(0F)
            .alpha(1F)
            .start()
    }

    fun animDown() {
        binding.textHelloWorld.animate()
            .translationY(0F)
            .alpha(1F)
            .start()
        binding.textHelloAndre.animate()
            .translationY(300.dp)
            .alpha(0F)
            .start()
    }

    val Int.dp: Float get() = (this / resources.displayMetrics.density)
}