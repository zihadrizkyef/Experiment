package com.olserapratama.printer.repository

data class Setting(
    var name: String = "",
    var brandId: Long? = null,
    var modelId: Long? = null,
    var deviceInterface: String = "",
    var address: String = "",
    var charCount: Int = 32,
    var numberCopy: Int = 1,
    var printLogo: Boolean = false,
)