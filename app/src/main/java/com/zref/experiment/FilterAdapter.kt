package com.zref.experiment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemFilterBinding

class FilterAdapter(private val list: List<Filter>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        val item = list[position]

        textName.text = item.name
        checkbox.clearOnCheckedStateChangedListeners()
        checkbox.isChecked = item.checked
        checkbox.addOnCheckedStateChangedListener { _, state ->
            item.checked = state == 1
        }
    }
}