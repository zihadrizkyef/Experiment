package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zref.experiment.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val listDouble = listOf(
            123_456_789.0,
            123_456_789.1,
            123_456_789.01,
            123_456_789.100,
            123_456_789.1001,
            123_456_789.1010,
            123_456_789.1100,
            123_456_789.1101,
            123_456_789.1110,
            123_456_789.1110000001,
            123_456_789.111000000,
            123_456_789.000000,
        )

        binding.text.text = listDouble.joinToString("\n")
        binding.text2.text = listDouble.joinToString("") { "->       " + it.toCurrency(false, "Rp") + "\n" }

        val listString = listOf(
            "Rp 123,456.0",
            "Rp 123,456.1",
            "Rp 123,456.01",
            "Rp 123,456.100",
            "Rp 123,456.1001",
            "Rp 123,456.1010",
            "Rp 123,456.1100",
            "Rp 123,456.1101",
            "Rp 123,456.1110",
            "Rp 123,456.1110000001",
            "Rp 123,456.111000000",
            "Rp 123,456.000000",
        )

        binding.text3.text = listString.joinToString("\n")
        binding.text4.text = listString.joinToString("") { "->       " + it.fromCurrency("$") + "\n" }
    }

    fun Double.toCurrency(showCurrency: Boolean = true, currency: String = "Rp "): String {
        val symbolConfig = if (currency.uppercase().startsWith("RP")) {
            val decFormat = DecimalFormatSymbols.getInstance()
            decFormat.groupingSeparator = '.'
            decFormat.decimalSeparator = ','
            decFormat
        } else {
            DecimalFormatSymbols.getInstance(Locale.US)
        }

        symbolConfig.currencySymbol = ""


        val formatter = NumberFormat.getInstance() as DecimalFormat
        formatter.decimalFormatSymbols = symbolConfig
        formatter.maximumFractionDigits = 340

        return if (showCurrency) {
            "$currency${formatter.format(this)}"
        } else {
            formatter.format(this)
        }
    }

    fun String.fromCurrency(currencySymbol: String = "Rp "): Double {
        val clean = if (currencySymbol.lowercase().startsWith("rp")) {
            this.replace(Regex("[^0-9,]"), "")
                .replace(",", ".")
        } else {
            this.replace(Regex("[^0-9.]"), "")
        }
        return clean.toDouble()
    }
}