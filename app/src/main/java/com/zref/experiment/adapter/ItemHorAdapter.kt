package com.zref.experiment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemHorBinding

class ItemHorAdapter(val listItem: ArrayList<String>) :
    RecyclerView.Adapter<ItemHorAdapter.TextViewHolder>() {

    inner class TextViewHolder(val binding: ItemHorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        Log.i("AOEU", "onCreateViewHolder()")
        val binding = ItemHorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.binding.textView.text = listItem[position]
    }
}