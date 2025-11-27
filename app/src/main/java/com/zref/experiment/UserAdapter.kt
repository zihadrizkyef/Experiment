package com.zref.experiment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zref.experiment.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun getItemCount() = UserDatabase.users.size

    private var holderCount = 0

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
        holder.binding.textView.text = (position+1).toString() + ". " + UserDatabase.users[position]
    }

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}