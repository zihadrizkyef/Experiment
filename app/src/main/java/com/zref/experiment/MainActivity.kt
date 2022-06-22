package com.zref.experiment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.qrcode)
        val scaled = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
        binding.imageView.setImageBitmap(bitmap)
        binding.imageView2.setImageBitmap(scaled)
    }
}