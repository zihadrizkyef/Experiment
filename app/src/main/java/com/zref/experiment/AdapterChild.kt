package com.zref.experiment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemChildBinding

class AdapterChild: RecyclerView.Adapter<AdapterChild.ViewHolder>() {
    class ViewHolder(val binding: ItemChildBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textChildName.text = position.toString()
    }
}