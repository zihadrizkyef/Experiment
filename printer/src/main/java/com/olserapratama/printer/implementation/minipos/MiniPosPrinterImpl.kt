package com.olserapratama.printer.implementation.minipos

import android.content.Context
import com.olserapratama.printer.util.IPrinter
import com.olserapratama.printer.util.PrintLine
import com.olserapratama.printer.repository.Setting
import timber.log.Timber
import kotlin.concurrent.thread

class MiniPosPrinterImpl (
    override val context: Context,
    override var connectedPrinter: Setting
) : IPrinter {
    override fun connect(listener: (message: String) -> Unit) {
        Timber.i("AOEU anuuuuuu")
        MiniPosFunctions.connect(context, connectedPrinter)
        Timber.i("AOEU anuuuuuu selesai")
    }

    override fun isConnected(): Boolean {
        return MiniPosFunctions.statusPrinter
    }

    override fun disconnect() {
        MiniPosFunctions.disconnect(connectedPrinter)
    }

    override fun printInvoice(lines: List<PrintLine>) {
        thread {
            MiniPosFunctions.printReceipt(context, lines, connectedPrinter)
        }
    }

    override fun openDrawer() {
        MiniPosFunctions.openDrawer()
    }
}