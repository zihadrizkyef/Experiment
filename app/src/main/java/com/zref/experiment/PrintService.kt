package com.zref.experiment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.olserapratama.printer.implementation.gprinter.GPrinterImpl
import com.olserapratama.printer.implementation.minipos.MiniPosPrinterImpl
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.Image
import com.olserapratama.printer.util.Line
import com.olserapratama.printer.util.TextCenter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class PrintService : Service() {
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val categories = arrayListOf<Category>()
    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Zihad", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))
        calendar.add(Calendar.SECOND, 15)
        categories.add(Category("Rizky", calendar.time))

        createTimerTask().run()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {
        val cat = categories.first()
        timer = Timer()
        timerTask = createTimerTask()
        timer!!.schedule(timerTask, cat.date)
        Timber.i("AOEU PrinterService schedule timer ${dateFormat.format(cat.date)}")
    }

    private fun createTimerTask() = object : TimerTask() {
        override fun run() {
            Timber.i("AOEU PrinterService timer run()")

            val categoryToPrint = categories.firstOrNull()
            if (categories.isNotEmpty()) {
                startForeground()
                Timber.i("AOEU PrinterService start timer")
                startTimer()
            } else {
                timer?.cancel()
                timer?.purge()
                stopSelf()
                Timber.i("AOEU PrinterService stop service")
            }

            if (categoryToPrint != null) {
                printCourseLine(categoryToPrint)
            } else {
//                val setting = Setting( //gprinter
//                    "ANU",
//                    0,
//                    0,
//                    "BT:",
//                    "DC:1D:30:40:A3:13"
//                )
//                val printer = GPrinterImpl(this@PrintService, setting)
                val setting = Setting( //minipos
                    "ANU",
                    0,
                    0,
                    "BT:",
                    "DC:0D:30:C1:2B:CB"
                )
                val printer = MiniPosPrinterImpl(this@PrintService, setting)
                if (!printer.isConnected()) {
                    printer.connect { }
                }
            }
            categories.remove(categoryToPrint)
        }
    }

    private fun startForeground() {
        val channelId = "olsera_course_line"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Print Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val cat = categories.first()
        var builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(cat.name)
            .setSmallIcon(R.mipmap.ic_launcher)
        if (categories.size > 1) {
            builder = builder.setContentText("Next: "+categories[1].name)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder = builder.setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setWhen(cat.date.time)
        }
        startForeground(1, builder.build())
    }

    private fun printCourseLine(category: Category) {
        val printTestLines = arrayListOf(
            Image("https://media.wired.com/photos/6053a7dcc34af78187790df0/191:100/w_1280,c_limit/Gear-Bunch-Bikes-IMG_2425.jpg", 300, 300),
            Line,
            TextCenter(category.name),
        )
//                val setting = Setting( //gprinter
//                    "ANU",
//                    0,
//                    0,
//                    "BT:",
//                    "DC:1D:30:40:A3:13"
//                )
//                val printer = GPrinterImpl(this@PrintService, setting)
        val setting = Setting( //minipos
            "ANU",
            0,
            0,
            "BT:",
            "DC:0D:30:C1:2B:CB"
        )
        val printer = MiniPosPrinterImpl(this@PrintService, setting)
//        if (printer.isConnected()) {
//            Timber.i("AOEU PrinterService printing $printer")
//            printer.printInvoice(printTestLines)
//        } else {
            Timber.i("AOEU PrinterService connecting $printer")
            printer.connect {}
//            Thread.sleep(3000L) //waiting for done connecting
            Timber.i("AOEU PrinterService printing $printer")
            printer.printInvoice(printTestLines)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("AOEU PrinterService onDestroy")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.i("AOEU PrinterService onLowMemory")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Timber.i("AOEU PrinterService onTaskRemoved")
    }
}