package com.olserapratama.printer.implementation.silicon

import android.content.Context
import com.olserapratama.printer.repository.DeviceInterface
import com.olserapratama.printer.util.IPrinter
import com.olserapratama.printer.util.PrintLine
import com.olserapratama.printer.repository.Setting
import kotlin.concurrent.thread

class SiliconPrinterImpl (
    override val context: Context,
    override var connectedPrinter: Setting
) : IPrinter {
    override fun connect(listener: (message: String) -> Unit) {
        thread {
            listener(SiliconFunctions.connect(context, connectedPrinter))
        }
    }

    override fun isConnected(): Boolean {
        return SiliconFunctions.statusPrinter
    }

    override fun disconnect() {
        SiliconFunctions.disconnect()
    }

    override fun printInvoice(lines: List<PrintLine>) {
        thread {
            SiliconFunctions.printReceipt(context, lines, connectedPrinter)
        }
    }

    override fun openDrawer() {
        SiliconFunctions.openDrawer()
    }
}

