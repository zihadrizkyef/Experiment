package com.zref.experiment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.olserapratama.printer.implementation.gprinter.GPrinterImpl
import com.olserapratama.printer.implementation.minipos.MiniPosPrinterImpl
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.zref.experiment.databinding.ActivityMainBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        startService(Intent(this, PrintService::class.java))
//        val dateFormat = SimpleDateFormat("HH:mm:ss")
//
//        Timer().schedule(object: TimerTask() {
//            override fun run() {
//                runOnUiThread {
//                    binding.text.text = dateFormat.format(Date())
//                }
//            }
//        }, 0, 1000)

        val setting = Setting( //gprinter
            "ANU",
            0,
            0,
            "BT:",
            "DC:1D:30:40:A3:13"
        )
        val printer = GPrinterImpl(this, setting)
//                val setting = Setting( //minipos
//                    "ANU",
//                    0,
//                    0,
//                    "BT:",
//                    "DC:0D:30:C1:2B:CB"
//                )
//                val printer = MiniPosPrinterImpl(this@PrintService, setting)
        printer.connect { }
        Handler().postDelayed({
            printer.printInvoice(listOf(Line))
        }, 2000)
        Handler().postDelayed({
            printer.connect { }
            Handler().postDelayed({
                printer.printInvoice(listOf(Line))
            }, 2000)
        }, 4000)
    }
}