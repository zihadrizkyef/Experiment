package com.zref.experiment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Next2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next2)

        Handler().postDelayed({
            val intent = Intent(this, Next3Activity::class.java)
            startActivity(intent)
        }, 1000)
    }
}