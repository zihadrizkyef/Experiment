package com.olserapratama.printer.util

sealed class PrintLine

data class EmptyLine(val lineCount: Int = 1): PrintLine()
data class TextCenter(val text: String): PrintLine()
data class TextLeft(val text: String): PrintLine()
data class TextRight(val text: String): PrintLine()
data class TextLeftRight(val left: String, val right: String): PrintLine()
data class Image(val url: String, val width: Int = 200, val height: Int = 200): PrintLine()
data class Barcode(val text: String, val width: Int = 200): PrintLine()
data class QRCode(val text: String, val width: Int = 200, val height: Int = 200): PrintLine()
object Line : PrintLine()