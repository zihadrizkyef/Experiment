package com.zref.experiment

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Next3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next3)

        findViewById<TextView>(R.id.text).setOnClickListener {
            val intent = Intent(this, NextActivity::class.java)
            intent.putExtra("anu", "SNTHASNTHASNHASNTAHSNTAHSNTAH")
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            startActivity(intent)
            finish()
        }
    }
}