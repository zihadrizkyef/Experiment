package com.zref.experiment

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.zref.experiment.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isFullChart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.textTitle.updatePadding(
                top = insets.top + 16f.dpToPx()
            )
            WindowInsetsCompat.CONSUMED
        }

        val markerView = MyMarkerView(this, R.layout.marker_view)
        binding.lineChart.marker = markerView

        binding.lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            isGranularityEnabled = true
            granularity = 1f
            valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String? {
                    return value.toInt().toString()
                }
            }
        }
        binding.lineChart.axisRight.isEnabled = false

        binding.textFooter.setOnClickListener {
            if (isFullChart) {
                setupLineChart(30, 6f)
            } else {
                setupLineChart(6, 1f)
            }
            isFullChart = !isFullChart
        }

        binding.textFooter.performClick()
    }

    private fun setupLineChart(count: Int, zoom: Float) {
        val entries = List(count) { Entry(it.toFloat(), Random.nextInt(100).toFloat()) }

        val dataSet = LineDataSet(entries, "Sample Data").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            setDrawValues(false)
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
        binding.lineChart.animateXY(500, 500)
        binding.lineChart.fitScreen()
        binding.lineChart.zoom(zoom, 1F, 0F, 0F)
    }
}
