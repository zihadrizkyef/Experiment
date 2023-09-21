package com.zref.experiment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.zref.experiment.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thread {
            val bitmap = Glide.with(this)
                .asBitmap()
                .load("https://e7.pngegg.com/pngimages/646/324/png-clipart-github-computer-icons-github-logo-monochrome.png")
                .apply(RequestOptions().override(200, 200).downsample(DownsampleStrategy.CENTER_INSIDE))
                .submit(200, 200)
                .get()
            runOnUiThread {
                binding.imageView.setImageBitmap(bitmap)
            }
        }
    }
}