package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val a = supportFragmentManager.beginTransaction()
        a.replace(binding.containerTop.id, FragmentList())
        a.replace(binding.containerBot.id, FragmentDetail())
        a.commit()
    }
}