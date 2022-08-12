package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val json = """{name:"zihad", length:"56.88"}"""
        val mobile = Gson().fromJson(json, Mobile::class.java)
        Log.i("AOEU", "mobile $mobile")
        Log.i("AOEU", "length ${mobile.length - 32}")
    }

    data class Mobile(val name: String, val length: Double)
}