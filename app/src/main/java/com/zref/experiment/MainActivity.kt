package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.zref.experiment.adapter.TextAdapter
import com.zref.experiment.databinding.ActivityMainBinding

/**
 * Experimenting [RecyclerView] with "wrap_content" height. Checking whether the "recycling" system
 * still work or not by putting [Log.i] in [Adapter.onCreateViewHolder]
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listItem = arrayListOf<String>()
    private val adapter by lazy { TextAdapter(listItem) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.buttonAdd.setOnClickListener {
            val lastSize = listItem.size
            repeat(59000) {
                listItem.add(it.toString())
            }
            adapter.notifyItemRangeInserted(lastSize, listItem.size)
        }
    }
}