package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CurrencyTextWatcher.applyTo(binding.edit1, "Rp ", '.', ',')
        CurrencyTextWatcher.applyTo(binding.edit2, "$ ", ',', '.')
        binding.buttonConvert.setOnClickListener {
            Log.i("AOEU", "anu1 ${CurrencyTextWatcher.getValue(binding.edit1)}")
            Log.i("AOEU", "anu2 ${CurrencyTextWatcher.getValue(binding.edit2)}")
        }

//        binding.edit1.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                binding.edit1.removeTextChangedListener(this)
//
//                binding.edit1.setText(binding.edit1.text.toString() + "1")
//
//                binding.edit1.addTextChangedListener(this)
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
    }
}