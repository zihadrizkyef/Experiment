package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = arrayListOf<Filter>()
        repeat(8) {
            list.addAll(listOf(
                Filter(false, "Zihad"),
                Filter(false, "Rizky"),
                Filter(false, "Edwin"),
                Filter(false, "Fikri"),
            ))
        }

        val adapter = FilterAdapter(list)
        binding.recyclerFilter.adapter = adapter
        binding.buttonShow.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.buttonClose.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }
    }
}