package com.zref.experiment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemNamaBinding

class NameAdapter : ListAdapter<Name, NameAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Name>() {
        override fun areItemsTheSame(
            oldItem: Name,
            newItem: Name
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Name,
            newItem: Name
        ): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.color == newItem.color
        }

    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNamaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val name = getItem(position)
        holder.binding.textName.text = name.name
        holder.binding.textName.setBackgroundColor(name.color)
    }

    class ViewHolder(val binding: ItemNamaBinding) : RecyclerView.ViewHolder(binding.root)


}