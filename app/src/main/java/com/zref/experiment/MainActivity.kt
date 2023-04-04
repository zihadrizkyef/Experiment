package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.zref.experiment.databinding.ActivityMainBinding
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
    }

    private fun setupChart() {
        binding.chart.setTouchEnabled(true)
        binding.chart.setPinchZoom(true)
        binding.chart.setDrawGridBackground(false)
        binding.chart.isHighlightPerTapEnabled = false
        binding.chart.isHighlightPerDragEnabled = false
        binding.chart.isClickable = false
        binding.chart.description.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.marker = null

        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.axisLeft.setDrawGridLines(true)
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.axisLeft.xOffset = 16F
        binding.chart.xAxis.yOffset = 16F
        binding.chart.axisRight.isEnabled = false
        binding.chart.axisLeft.valueFormatter = object : ValueFormatter() {
            val decimalFormat = DecimalFormat()
            override fun getFormattedValue(value: Float): String {
                return "${decimalFormat.format(value)}jt"
            }
        }

        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            val decimalFormat = DecimalFormat()
            override fun getFormattedValue(value: Float): String {
                return if (value > 0) {
                    "${decimalFormat.format(value)} Okt"
                } else {
                    ""
                }
            }
        }

        val entries = generateLineDataSet()
        val data = LineData()
        data.addDataSet(entries)

        binding.chart.xAxis.axisMinimum = entries.xMin - 1
        binding.chart.xAxis.axisMaximum = entries.xMax + 1
        binding.chart.xAxis.setLabelCount(min(10, ((entries.xMax + 1) - (entries.xMin - 1)).toInt() + 1), false)

        binding.chart.axisLeft.axisMinimum = entries.yMin - 1
        binding.chart.axisLeft.axisMaximum = entries.yMax + 1
        binding.chart.axisLeft.setLabelCount(min(7, ((entries.yMax + 1) - (entries.yMin - 1)).toInt() + 1), false)

        binding.chart.data = data
        binding.chart.invalidate()
    }

    private fun generateLineDataSet(): ILineDataSet {
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 3f))
        entries.add(Entry(2f, 3f))
        entries.add(Entry(3f, 5f))
        entries.add(Entry(4f, 6f))
        entries.add(Entry(5f, 7f))
        entries.add(Entry(6f, 8f))
        entries.add(Entry(7f, 9.5f))
        entries.add(Entry(8f, 9f))
        entries.add(Entry(9f, 9f))
        entries.add(Entry(10f, 11f))
        entries.add(Entry(11f, 12f))
        entries.add(Entry(12f, 14f))
        entries.add(Entry(13f, 13f))
        entries.add(Entry(14f, 15f))

        val dataSet = LineDataSet(entries, "Label")
        dataSet.color = ContextCompat.getColor(this, R.color.brand)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.purple_500)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)

        return dataSet
    }
}