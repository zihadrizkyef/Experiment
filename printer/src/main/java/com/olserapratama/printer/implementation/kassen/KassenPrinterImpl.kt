package com.olserapratama.printer.implementation.kassen

import android.content.Context
import com.olserapratama.printer.util.IPrinter
import com.olserapratama.printer.util.PrintLine
import com.olserapratama.printer.repository.Setting
import kotlin.concurrent.thread

class KassenPrinterImpl (
    override val context: Context,
    override var connectedPrinter: Setting
) : IPrinter {
    override fun connect(listener: (message: String) -> Unit) {
        listener(KassenFunctions.connect(context, connectedPrinter))
    }

    override fun isConnected(): Boolean {
        return KassenFunctions.statusPrinter
    }

    override fun disconnect() {
        KassenFunctions.disconnect(connectedPrinter)
    }

    override fun printInvoice(lines: List<PrintLine>) {
        thread {
            KassenFunctions.printReceipt(context, lines, connectedPrinter.charCount)
        }
    }

    override fun openDrawer() {
        KassenFunctions.openDrawer()
    }

}