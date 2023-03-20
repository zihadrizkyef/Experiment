package com.olserapratama.printer.implementation.minipos

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.olserapratama.printer.R
import com.olserapratama.printer.libs.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos
import com.olserapratama.printer.repository.DeviceInterface
import com.olserapratama.printer.repository.Setting
import com.olserapratama.printer.util.*
import com.olserapratama.printer.util.PrinterUtil.stringToBarcode
import com.olserapratama.printer.util.PrinterUtil.stringToQrCode
import com.sun.jna.Pointer

object MiniPosFunctions {
    var statusPrinter = false
    private var h = Pointer.NULL

    fun connect(context: Context, printerSetting: Setting): String {
        when (printerSetting.deviceInterface) {
            DeviceInterface.BLUETOOTH.code -> {
                return try {
                    h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(printerSetting.address)
                    statusPrinter = true
                    "Printer is connected!"
                } catch (e: Exception) {
                    statusPrinter = false
                    e.message
                    e.message.toString()
                }
            }
            DeviceInterface.WIFI.code -> {
                return "Printer is not connected!"
            }
            else -> {
                return try {
                    val devicePaths = printerlibs_caysnpos.CaysnPos_EnumUsbVidPid_Helper.CaysnPos_EnumUsbVidPidA()
                    if (devicePaths.isNotEmpty()) {
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
        }
    }

    fun printReceipt(context: Context, lines: List<PrintLine>, printerSetting: Setting): Boolean {
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
                        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextA(h, PrinterUtil.formatLeftRight(it.left, it.right, printerSetting.charCount) + "\n")
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
                        for (i in 0 until printerSetting.charCount) {
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
            printerSetting.modelId?.toInt().let {
                if (it != null) {
                    if (it > 1){
                        printerlibs_caysnpos.INSTANCE.CaysnPos_FeedLine(h, 2)
                    }
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

    fun openDrawer(): Boolean{
        try {
            val result = printerlibs_caysnpos.INSTANCE.CaysnPos_KickOutDrawer(h, 0, 100, 100)
            if (result == 0) {
                return false
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}