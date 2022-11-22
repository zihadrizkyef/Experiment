package com.zref.experiment

import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.widget.EditText
import java.io.File.separator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import kotlin.math.max

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/03/18.
 */

/**
 * TextWatcher for EditText which is containing money formatted text
 */
class CurrencyTextWatcher(val editText: EditText) : TextWatcher {

    companion object {
        fun applyTo(
            editText: EditText,
            currency: String = "$ ",
            separator: Char = ',',
            decimal: Char = '.',
        ) {
            val watcher = CurrencyTextWatcher(editText).apply {
                this.currency = currency
                this.separator = separator
                this.decimal = decimal
            }
            editText.addTextChangedListener(watcher)
        }

        fun getValue(editText: EditText, currency: String = ""): Double {
            var decimal = '.'
            var separator = ','

            if (currency.isBlank()) {
                if (editText.text.toString().lowercase().startsWith("rp")) {
                    decimal = ','
                    separator = '.'
                }
            } else {
                if (currency.lowercase().startsWith("rp")) {
                    decimal = ','
                    separator = '.'
                }
            }

            val numberAndSymbol = editText.text.toString().dropWhile { !it.isDigit() && it != decimal && it != separator }
            val symbolFormatter = DecimalFormatSymbols()
            symbolFormatter.groupingSeparator = separator
            symbolFormatter.decimalSeparator = decimal
            val formatter = DecimalFormat()
            formatter.decimalFormatSymbols = symbolFormatter
            return formatter.parse(numberAndSymbol)!!.toDouble()
        }
    }

    var currency = "$ "
        set(value) {
            startEditablePos = value.length
            val oldMoneyPrefix = currency
            field = value
            val moneyText = editText.text.toString()
            var startSelectionPos = editText.selectionStart
            editText.setText(moneyText.replaceFirst(oldMoneyPrefix, value))
            if (startSelectionPos > oldMoneyPrefix.length) {
                startSelectionPos -= oldMoneyPrefix.length
                startSelectionPos += value.length
            } else {
                if (startSelectionPos > 0) {
                    startSelectionPos = value.length
                }
            }
            editText.setSelection(startSelectionPos)
        }
    private var separator = ','
        set(value) {
            val moneyText = editText.text.toString()
            val startSelectionPos = editText.selectionStart
            val oldSeparator = separator
            field = value
            editText.setText(moneyText.replace(oldSeparator, value))
            editText.setSelection(startSelectionPos)
        }
    private var decimal = '.'
        set(value) {
            val moneyText = editText.text.toString()
            val startSelectionPos = editText.selectionStart
            val oldDecimal = decimal
            field = value
            editText.setText(moneyText.replace(oldDecimal, value))
            editText.setSelection(startSelectionPos)
            editText.keyListener = DigitsKeyListener.getInstance("0123456789$decimal")
        }

    private var prefString = ""
    private var prefCursorStartPos = -1
    private var prefCursorEndPos = -1
    private var startEditablePos = currency.length

    init {
        editText.inputType = InputType.TYPE_CLASS_PHONE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        editText.setText(currency)
        editText.setSelection(startEditablePos)
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        prefString = s.toString()
        prefCursorStartPos = editText.selectionStart
        prefCursorEndPos = editText.selectionEnd
    }

    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, lengthInserted: Int) {
        s?.let {
            editText.removeTextChangedListener(this)

            var cursorPos = editText.selectionStart
            val actionIsWrite = (prefString.length < it.length)
            val actionIsDelete = (prefString.length > it.length)
            var textToFormat = it

            //prevent to delete or write in [currency]
            if (prefCursorStartPos <= startEditablePos) {
                if (actionIsWrite) {
                    val textInserted = it.substring(start until start + lengthInserted)
                    textToFormat = currency + textInserted + prefString.substring(currency.length)
                    cursorPos = currency.length + textInserted.length
                } else if (actionIsDelete) {
                    cursorPos = currency.length
                    if (prefString.length > currency.length) { //If edit text is not just [currency] (there's number)
                        if (prefCursorEndPos == -1 || prefCursorEndPos == prefCursorStartPos) { //If not selecting text
                            textToFormat = currency + prefString.substring(currency.length + 1) //delete first number
                        } else { //If selecting part or all text
                            textToFormat = currency + prefString.substring(prefCursorEndPos) //delete selected number but keep [currency]
                        }
                    } else {
                        textToFormat = currency
                    }
                }
            }

            //prevent to write decimal as first char
            if (actionIsWrite && textToFormat.startsWith(currency + decimal)) {
                textToFormat = textToFormat.replaceFirst(Regex("$currency(\\$decimal)+"), currency)
                cursorPos = startEditablePos
            }

            if (textToFormat.length > currency.length) {
                //delete money prefix
                var filteredText = textToFormat.substring(currency.length)
                cursorPos = max(0, cursorPos - currency.length)

                //use only first decimal symbol
                while (filteredText.count { it == decimal } > 1) {
                    val lastDecimalPos = filteredText.lastIndexOf(decimal)
                    filteredText = filteredText.removeLastChar(decimal)
                    if (lastDecimalPos < cursorPos) {
                        cursorPos--
                    }
                }

                //delete all $separator and use first decimal symbol
                for (i in cursorPos - 1 downTo 0) {
                    if (filteredText[i] == separator) {
                        cursorPos--
                    }
                }
                filteredText = filteredText.replace(separator.toString(), "")

                //delete 0 in the begining
                var zeroCount = 0
                val prefCursorPos = cursorPos
                for (i in 0 until filteredText.length) {
                    if (filteredText[i] == '0') {
                        zeroCount++
                        if (i < prefCursorPos) {
                            cursorPos--
                        }
                    } else {
                        break
                    }
                }
                filteredText = filteredText.substring(zeroCount)

                //add new separator
                val separatorInserter = StringBuilder(filteredText)
                val decimalPos = filteredText.indexOf(decimal)
                val separatorStart = if (decimalPos > -1) decimalPos - 3 else (filteredText.length - 3)
                for (i in separatorStart downTo 1 step 3) {
                    if (i < cursorPos) {
                        cursorPos += 1
                    }
                    separatorInserter.insert(i, separator)
                }
                filteredText = separatorInserter.toString()

                //add currency
                filteredText = currency + filteredText
                cursorPos += currency.length

                editText.setText(filteredText)
                editText.setSelection(cursorPos)
            } else {
                editText.setText(textToFormat)
                editText.setSelection(cursorPos)
            }

            editText.addTextChangedListener(this)
        }
    }

    fun setSymbol(decimal: Char, separator: Char) {
        if (separator == decimal) {
            throw UnsupportedOperationException("decimal should not same as separator")
        } else {
            this.decimal = decimal
            this.separator = separator
        }
    }

    fun String.removeLastChar(char: Char): String {
        return substringBeforeLast(char) + substringAfterLast(char)
    }
}