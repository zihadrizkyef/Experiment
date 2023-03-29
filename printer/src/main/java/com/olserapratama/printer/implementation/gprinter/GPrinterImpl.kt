package com.olserapratama.printer.implementation.gprinter

import android.content.Context
import com.olserapratama.printer.util.IPrinter
import com.olserapratama.printer.util.PrintLine
import com.olserapratama.printer.repository.Setting
import timber.log.Timber
import kotlin.concurrent.thread

class GPrinterImpl (
    override val context: Context,
    override var connectedPrinter: Setting
) : IPrinter {
    override fun connect(listener: (message: String) -> Unit) {
        Timber.i("AOEU printer aaaaaa")
        GPrinterFunctions.connect(context, connectedPrinter, listener)
    }

    override fun isConnected(): Boolean {
        return GPrinterFunctions.statusPrinter
    }

    override fun disconnect() {
        GPrinterFunctions.disconnect()
    }

    override fun printInvoice(lines: List<PrintLine>) {
        thread {
            GPrinterFunctions.printReceipt(context, lines, connectedPrinter)
        }
    }

    override fun openDrawer() {
        GPrinterFunctions.openDrawer()
    }
}