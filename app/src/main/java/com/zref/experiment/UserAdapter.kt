package com.zref.experiment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var holderCount = 0
    private var list = UserDatabase.users

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        holderCount++
        Log.i("AOEU", "onCreateViewHolder called $holderCount times")
        val binding = ItemUserBinding.inflate(
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
        Log.i("AOEU", "onBindViewHolder called for position $position")
        holder.binding.textView.text =
            (position + 1).toString() + ". " + list[position]
    }

    fun searchName(name: String) {
        list = UserDatabase.users.filter { it.contains(name, true) }
        notifyDataSetChanged()

        Log.i("AOEU", list.joinToString("\n"))
    }

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}