package com.olserapratama.printer.implementation.gprinter

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.gprinter.command.EscCommand
import com.gprinter.command.LabelCommand
import com.olserapratama.printer.R
import com.olserapratama.printer.libs.gprinterlibs.GPDeviceConnFactoryManager
import com.olserapratama.printer.repository.DeviceInterface
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.olserapratama.printer.util.PrinterUtil.stringToBarcode
import com.olserapratama.printer.util.PrinterUtil.stringToQrCode
import timber.log.Timber
import java.util.*
import kotlin.concurrent.thread


object GPrinterFunctions {
    var statusPrinter = false

    fun connect(context: Context, printerSetting: Setting, listener: (message: String) -> Unit) {
        Timber.i("AOEU printer building")
        when (printerSetting.deviceInterface) {
            DeviceInterface.BLUETOOTH.code -> {
                GPDeviceConnFactoryManager.Build()
                    .setId(0)
                    .setContext(context)
                    .setName(printerSetting.name)
                    .setConnMethod(GPDeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                    .setMacAddress(printerSetting.address)
                    .build()
            }
            DeviceInterface.WIFI.code -> {
                GPDeviceConnFactoryManager.Build()
                    .setId(0)
                    .setContext(context)
                    .setName(printerSetting.name)
                    .setConnMethod(GPDeviceConnFactoryManager.CONN_METHOD.WIFI)
                    .setIp(printerSetting.address)
                    .setPort(9600)
                    .build()

            }
            else -> {
                val mUsbManger = context.getSystemService(Context.USB_SERVICE) as UsbManager
                val deviceList = mUsbManger.deviceList
                if (deviceList.isNotEmpty()) {
                    val usbDevice = deviceList.values.elementAt(0)

                    GPDeviceConnFactoryManager.Build()
                        .setId(0)
                        .setContext(context)
                        .setName(printerSetting.name)
                        .setConnMethod(GPDeviceConnFactoryManager.CONN_METHOD.USB)
                        .setUsbDevice(usbDevice)
                        .setPort(0)
                        .build()
                }
            }
        }

        try {
            statusPrinter = if (GPDeviceConnFactoryManager.getDeviceConnFactoryManagers().isNotEmpty() && GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] != null) {
                thread {
                    val handler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        listener(context.getString(R.string.printer_connected))
                        Timber.i("AOEU printer connected")
                    }
                    handler.postDelayed(runnable, 3000)
                    try {
                        Timber.i("AOEU printer connecting")
                        GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].openPort()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        handler.removeCallbacks(runnable)
                        Timber.i("AOEU printer failed to connect")
                    }
                }
                true
            } else {
                Timber.i("AOEU printer failed to connect")
                false
            }
        } catch (e: Exception) {
            Timber.i("AOEU printer failed to connect")
            statusPrinter = false
        }
    }

    fun disconnect() {
        if (GPDeviceConnFactoryManager.getDeviceConnFactoryManagers().isNotEmpty()) {
            GPDeviceConnFactoryManager.closeAllPort()
            statusPrinter = false
        }
    }

    fun printReceipt(context: Context, lines: List<PrintLine>, printerSetting: Setting): Boolean {
        for (i in GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()) {
            println("LIST GPDEVICE: $i")
        }
//        if (GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] == null ||
//            gPrinter?.connState == false
//        ) { return false }

        try {
            val esc = EscCommand()
            esc.addInitializePrinter()
            lines.forEach {
                when (it) {
                    is EmptyLine -> {
                        esc.addText("\n".repeat(it.lineCount))
                    }
                    is TextCenter -> {
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
                        esc.addText(it.text + "\n")
                    }
                    is TextLeft -> {
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
                        esc.addText(it.text + "\n")
                    }
                    is TextRight -> {
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT)
                        esc.addText(it.text + "\n")
                    }
                    is TextLeftRight -> {
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
                        esc.addText(PrinterUtil.formatLeftRight(it.left, it.right, printerSetting.charCount) + "\n")
                    }
                    is Image -> {
                        val bitmap = Glide.with(context)
                            .asBitmap()
                            .load(it.url)
                            .apply(RequestOptions().override(it.width, it.height).downsample(DownsampleStrategy.CENTER_INSIDE))
                            .submit(it.width, it.height)
                            .get()

                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
                        esc.addRastBitImage(bitmap, it.width, 0)
                        esc.addPrintAndFeedLines(1.toByte())
                    }
                    is Line -> {
                        var line = ""
                        for (i in 0 until printerSetting.charCount) {
                            line += "-"
                        }
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
                        esc.addText(line + "\n")
                    }
                    is Barcode -> {
                        val bitmap = stringToBarcode(it.text, it.width)
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
                        esc.addRastBitImage(bitmap, it.width, 0)
                        esc.addPrintAndFeedLines(1.toByte())
                    }
                    is QRCode -> {
                        val bitmap = stringToQrCode(it.text, it.width, it.height)
                        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
                        esc.addRastBitImage(bitmap, it.width, 0)
                        esc.addPrintAndFeedLines(1.toByte())
                    }
                }
            }
            esc.addText("\n".repeat(2))
            val datas: Vector<Byte> = esc.command

            return GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(datas)
//            return gPrinter?.sendDataImmediately(datas)!!

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun openDrawer(): Boolean {
        if (GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] == null ||
            !GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].connState
        ) {
            return false
        }

        val esc = EscCommand()
        esc.addInitializePrinter()

        esc.addGeneratePlus(LabelCommand.FOOT.F5, 255.toByte(), 255.toByte())
        esc.addGeneratePlus(LabelCommand.FOOT.F2, 255.toByte(), 255.toByte())

        val datas = esc.command
        return GPDeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].sendDataImmediately(datas)
    }

}