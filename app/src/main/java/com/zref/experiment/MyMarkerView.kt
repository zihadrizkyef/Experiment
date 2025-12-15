package com.zref.experiment

import android.annotation.SuppressLint
import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.zref.experiment.databinding.MarkerViewBinding

@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val binding: MarkerViewBinding

    init {
        binding = MarkerViewBinding.bind(getChildAt(0))
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        binding.tvContent.text = e?.y?.toInt()?.toString() ?: ""
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
