package com.olserapratama.printer.implementation.iware

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.epson.epos2.printer.Printer
import com.olserapratama.printer.R
import com.olserapratama.printer.libs.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos
import com.olserapratama.printer.repository.DeviceInterface
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.olserapratama.printer.util.PrinterUtil.stringToBarcode
import com.olserapratama.printer.util.PrinterUtil.stringToQrCode
import com.sun.jna.Pointer
import timber.log.Timber

object IwareFunctions {
    private var printer: Printer? = null
    var statusPrinter = false
    private var h = Pointer.NULL

    fun connect(context: Context, printerSetting: Setting): String {
        when (printerSetting.deviceInterface) {
            DeviceInterface.BLUETOOTH.code -> {
                return try {
                    h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(printerSetting.address)
                    statusPrinter = true
                    context.getString(R.string.printer_connected)
                } catch (e: Exception) {
                    statusPrinter = false
                    e.message
                    e.message.toString()
                }
            }
            DeviceInterface.WIFI.code -> {
                return try {
                    printer = printerSetting.modelId?.toInt()?.let { Printer(it, 0, context) }
                    printer?.setConnectionEventListener { any, i -> Timber.d("AOEU connection $any $i") }
                    printer?.setReceiveEventListener { printer, i, printerStatusInfo, s -> Timber.d("AOEU event $printer $i $printerStatusInfo $s") }
                    printer?.setStatusChangeEventListener { printer, i -> Timber.d("AOEU status $printer $i") }

                    val address = printerSetting.deviceInterface + printerSetting.address
                    printer?.connect(address, Printer.PARAM_DEFAULT)
                    statusPrinter = true
                    context.getString(R.string.printer_connected)
                } catch (e: Exception) {
                    printer = null
                    statusPrinter = false
                    e.message.toString()
                }
            } else -> {
                return try {
                    val devicePaths = printerlibs_caysnpos.CaysnPos_EnumUsbVidPid_Helper.CaysnPos_EnumUsbVidPidA()
                    if (devicePaths.isNotEmpty()){
                        printerSetting.address = devicePaths[0]
                    }
                    h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenUsbVidPidStringA(printerSetting.address)
                    statusPrinter = true
                    context.getString(R.string.printer_connected)
                } catch (ex: Exception) {
                    statusPrinter = false
                    ex.message.toString()
                }
            }
        }
    }

    fun disconnect(printerSetting: Setting) {
        if (printerSetting.deviceInterface == DeviceInterface.BLUETOOTH.code || printerSetting.deviceInterface == DeviceInterface.USB.code){
            if (h !== Pointer.NULL) {
                printerlibs_caysnpos.INSTANCE.CaysnPos_Close(h)
                h = Pointer.NULL
            }
            statusPrinter = false
        } else {
            printer?.disconnect()
            printer = null
        }
    }

    fun printUsingBluetooth(context: Context, lines: List<PrintLine>, charCount: Int): Boolean{
        try {
            val result: Int = printerlibs_caysnpos.INSTANCE.CaysnPos_ResetPrinter(h)
            if (result == 0) {
                statusPrinter = false
                return false
            }

            lines.forEach {
                when (it) {
                    is EmptyLine -> {
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, "\n".repeat(it.lineCount))
                    }
                    is TextCenter -> {
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, it.text + "\n")
                    }
                    is TextLeft -> {
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_Left)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, it.text + "\n")
                    }
                    is TextRight -> {
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_Right)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, it.text + "\n")
                    }
                    is TextLeftRight -> {
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, 0)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, PrinterUtil.formatLeftRight(it.left, it.right, charCount) + "\n")
                    }
                    is Image -> {
                        val bitmap = Glide.with(context)
                            .asBitmap()
                            .load(it.url)
                            .apply(RequestOptions().override(it.width, it.height).downsample(DownsampleStrategy.CENTER_INSIDE))
                            .submit(it.width, it.height)
                            .get()

                        if (bitmap != null) {
                            val width: Int = bitmap.width
                            val height: Int = bitmap.height
                            val pageWidth = 384
                            var dstw = width
                            var dsth = height
                            if (dstw > pageWidth) {
                                dstw = pageWidth
                                dsth = (dstw * (height.toDouble() / width)).toInt()
                            }
                            printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter)
                            printerlibs_caysnpos.CaysnPos_PrintRasterImage_Helper.CaysnPos_PrintRasterImageFromBitmap(h, dstw, dsth, bitmap, 0)

                        }
                    }
                    is Line -> {
                        var line = ""
                        for (i in 0 until charCount) {
                            line += "-"
                        }
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_Left)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, line + "\n")
                    }
                    is Barcode -> {
                        val bitmap = stringToBarcode(it.text, it.width)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter)
                        printerlibs_caysnpos.CaysnPos_PrintRasterImage_Helper.CaysnPos_PrintRasterImageFromBitmap(h, it.width, bitmap.height, bitmap, 0)
                    }
                    is QRCode -> {
                        val bitmap = stringToQrCode(it.text, it.width, it.height)
                        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter)
                        printerlibs_caysnpos.CaysnPos_PrintRasterImage_Helper.CaysnPos_PrintRasterImageFromBitmap(h, it.width, it.height, bitmap, 0)
                    }
                    else -> {}
                }
            }
            printerlibs_caysnpos.INSTANCE.CaysnPos_FeedLine(h, 1)
            printerlibs_caysnpos.INSTANCE.CaysnPos_FullCutPaper(h)

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun printUsingWifi(context: Context, lines: List<PrintLine>, printerSetting: Setting): Boolean{
        if (printer == null){
            connect(context, printerSetting)
        } else {
            printer?.beginTransaction()
        }

        lines.forEach {
            when (it) {
                is EmptyLine -> {
                    printer?.addFeedLine(it.lineCount)
                }
                is TextCenter -> {
                    printer?.addTextAlign(Printer.ALIGN_CENTER)
                    printer?.addText(it.text+"\n")
                }
                is TextLeft -> {
                    printer?.addTextAlign(Printer.ALIGN_LEFT)
                    printer?.addText(it.text+"\n")
                }
                is TextRight -> {
                    printer?.addTextAlign(Printer.ALIGN_RIGHT)
                    printer?.addText(it.text+"\n")
                }
                is TextLeftRight -> {
                    printer?.addTextAlign(Printer.ALIGN_LEFT)
                    printer?.addText(PrinterUtil.formatLeftRight(it.left, it.right, printerSetting.charCount)+"\n")
                }
                is Image -> {
                    val bitmap = Glide.with(context)
                        .asBitmap()
                        .load(it.url)
                        .apply(RequestOptions().override(it.width, it.height).downsample(DownsampleStrategy.CENTER_INSIDE))
                        .submit(it.width, it.height)
                        .get()

                    printer?.addTextAlign(Printer.ALIGN_CENTER)
                    printer?.addImage(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        Printer.COLOR_1,
                        Printer.MODE_MONO,
                        Printer.HALFTONE_DITHER,
                        Printer.PARAM_DEFAULT.toDouble(),
                        Printer.COMPRESS_AUTO
                    )
                }
                is Line -> {
                    printer?.addTextAlign(Printer.ALIGN_CENTER)
                    printer?.addText("-".repeat(printerSetting.charCount)+"\n")
                }
                is Barcode -> {
                    val bitmap = stringToBarcode(it.text, it.width)
                    printer?.addTextAlign(Printer.ALIGN_CENTER)
                    printer?.addImage(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        Printer.COLOR_1,
                        Printer.MODE_MONO,
                        Printer.HALFTONE_DITHER,
                        Printer.PARAM_DEFAULT.toDouble(),
                        Printer.COMPRESS_AUTO
                    )
                }
                is QRCode -> {
                    val bitmap = stringToQrCode(it.text, it.width, it.height)
                    printer?.addTextAlign(Printer.ALIGN_CENTER)
                    printer?.addImage(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        Printer.COLOR_1,
                        Printer.MODE_MONO,
                        Printer.HALFTONE_DITHER,
                        Printer.PARAM_DEFAULT.toDouble(),
                        Printer.COMPRESS_AUTO
                    )
                }
                else -> {}
            }
        }
        printer?.addText("\n\n")
        printer?.addCut(Printer.CUT_FEED)
        printer?.sendData(Printer.PARAM_DEFAULT)
        printer?.clearCommandBuffer()
        printer?.endTransaction()

        return true
    }

    fun openDrawerBluetooth(){
        try {
            val result = printerlibs_caysnpos.INSTANCE.CaysnPos_KickOutDrawer(h, 0, 100, 100)
            if (result == 0) {
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openDrawerWifi(){
        try {
            printer?.addPulse(0, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}