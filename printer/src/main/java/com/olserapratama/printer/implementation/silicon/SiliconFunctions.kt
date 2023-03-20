package com.olserapratama.printer.implementation.silicon

import android.content.Context
import android.graphics.Bitmap
import com.RT_Printer.WIFI.WifiPrintDriver
import com.olserapratama.printer.repository.DeviceInterface
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.olserapratama.printer.util.PrinterUtil.bitmapToByteArray
import com.olserapratama.printer.util.PrinterUtil.getLogoBitmapForReceipt
import com.olserapratama.printer.util.PrinterUtil.stringToBarcode
import com.olserapratama.printer.util.PrinterUtil.stringToQrCode
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions


object SiliconFunctions {
    var statusPrinter = false

    fun connect(context: Context, printerSetting: Setting): String{
        var message = ""
        when (printerSetting.deviceInterface) {
            DeviceInterface.BLUETOOTH.code -> {

            }
            DeviceInterface.WIFI.code -> {
                if(!WifiPrintDriver.WIFISocket(printerSetting.address, 9100)) {
                    WifiPrintDriver.Close()
                    return "Failed connect to : "
                }
            }
            else -> {

            }
        }
        return message
    }

    fun disconnect(){
        try {
            WifiPrintDriver.Close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun printReceipt(context: Context, lines: List<PrintLine>, printerSetting: Setting): Boolean {
        return try {
            WifiPrintDriver.Begin()
            lines.forEach {
                when (it) {
                    is EmptyLine -> {
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        WifiPrintDriver.ImportData("\n".repeat(it.lineCount))
                        WifiPrintDriver.excute()
                        //WifiPrintDriver.ClearData()
                    }
                    is TextCenter -> {
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        WifiPrintDriver.ImportData(it.text + "\n")
                        WifiPrintDriver.excute()
                    }
                    is TextLeft -> {
                        WifiPrintDriver.AddAlignMode(0.toByte())
                        WifiPrintDriver.ImportData(it.text + "\n")
                        WifiPrintDriver.excute()
                    }
                    is TextRight -> {
                        WifiPrintDriver.AddAlignMode(2.toByte())
                        WifiPrintDriver.ImportData(it.text + "\n")
                        WifiPrintDriver.excute()
                    }
                    is TextLeftRight -> {
                        WifiPrintDriver.AddAlignMode(0.toByte())
                        WifiPrintDriver.ImportData(PrinterUtil.formatLeftRight(it.left, it.right, printerSetting.charCount) + "\n" )
                        WifiPrintDriver.excute()
                    }
                    is Image -> {
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        val bitmap: Bitmap = getLogoBitmapForReceipt(context, it.url, it.width, it.height, 576.0)

                        /*val bitmap = Glide.with(context)
                            .asBitmap()
                            .load(it.url)
                            .apply(RequestOptions().override(it.width, it.height).downsample(DownsampleStrategy.CENTER_INSIDE))
                            .submit(it.width, it.height)
                            .get()*/

                        val command: ByteArray = bitmapToByteArray(bitmap)
                        WifiPrintDriver.printByteData(command)
                        WifiPrintDriver.excute()
                    }
                    is Line -> {
                        var line = ""
                        for (i in 0 until printerSetting.charCount) {
                            line += "-"
                        }
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        WifiPrintDriver.ImportData(line + "\n")
                        WifiPrintDriver.excute()
                    }
                    is Barcode -> {
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        val bitmap = stringToBarcode(it.text, it.width)
                        val command: ByteArray = bitmapToByteArray(bitmap)
                        WifiPrintDriver.printByteData(command)
                        WifiPrintDriver.excute()
                    }
                    is QRCode -> {
                        WifiPrintDriver.AddAlignMode(1.toByte())
                        val bitmap = stringToQrCode(it.text, it.width, it.height)
                        val command: ByteArray = bitmapToByteArray(bitmap)
                        WifiPrintDriver.printByteData(command)
                        WifiPrintDriver.excute()
                    }
                }
            }

            WifiPrintDriver.ImportData("\n".repeat(2))
            WifiPrintDriver.LF()
            WifiPrintDriver.excute()
            WifiPrintDriver.ClearData()
            WifiPrintDriver.printParameterSet(byteArrayOf(29, 86, 49))
            true
        } catch (e: Exception){
            false
        }
    }

    fun openDrawer() {
        try {
            WifiPrintDriver.printParameterSet(byteArrayOf(27, 112, 48, 55, 121))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

}