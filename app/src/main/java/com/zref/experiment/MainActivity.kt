package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.zref.experiment.adapter.ItemHorAdapter
import com.zref.experiment.adapter.ItemVerAdapter
import com.zref.experiment.databinding.ActivityMainBinding

/**
 * Experimenting [RecyclerView] with "wrap_content" height. Checking whether the "recycling" system
 * still work or not by putting [Log.i] in [Adapter.onCreateViewHolder]
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listItem = arrayListOf<String>()
    private val adapterVer by lazy { ItemVerAdapter(listItem) }
    private val adapterHor by lazy { ItemHorAdapter(listItem) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView1.adapter = adapterHor
        binding.recyclerView2.adapter = adapterVer

        binding.buttonAdd.setOnClickListener {
            val lastSize = listItem.size
            repeat(59000) {
                listItem.add(it.toString())
            }
            adapterHor.notifyItemRangeInserted(lastSize, listItem.size)
            adapterVer.notifyItemRangeInserted(lastSize, listItem.size)
        }
    }
}