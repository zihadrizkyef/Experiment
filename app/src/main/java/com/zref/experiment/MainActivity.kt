package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.olserapratama.printer.implementation.gprinter.GPrinterImpl
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val printTestLines = arrayListOf(
            Image("https://media.wired.com/photos/6053a7dcc34af78187790df0/191:100/w_1280,c_limit/Gear-Bunch-Bikes-IMG_2425.jpg", 300, 300),
            Line,
            TextCenter("TOKOKU\nJALAN Timur Raya No. 7, Sleman, Yogyakarta 55288, +6284744284888"),
        )
        val setting = Setting(
            "ANU",
            0,
            0,
            "BT:",
            "DC:1D:30:40:A3:13"
        )
        val printer = GPrinterImpl(this, setting)
        printer.connect { }
        Handler().postDelayed({ printer.printInvoice(printTestLines) }, 5000)
    }
}