package com.zref.experiment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.zref.experiment.databinding.ItemParentBinding

class AdapterParent : RecyclerView.Adapter<AdapterParent.ViewHolder>() {
    private val pool = RecyclerView.RecycledViewPool()
    class ViewHolder(val binding: ItemParentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(ItemParentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        val mScrollTouchListener: OnItemTouchListener = object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        holder.binding.recyclerChild.addOnItemTouchListener(mScrollTouchListener)
        return holder
    }

    override fun getItemCount(): Int = 100

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setBackgroundColor(Color.rgb(listOf(0, 255).random(), listOf(0, 255).random(), listOf(0, 255).random()))
        holder.binding.textParentName.text = position.toString()
        holder.binding.recyclerChild.setRecycledViewPool(pool)
        holder.binding.recyclerChild.swapAdapter(AdapterChild(), true)
    }
}