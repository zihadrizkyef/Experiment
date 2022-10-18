package com.zref.experiment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        Handler().postDelayed({
            val intent = Intent(this, Next2Activity::class.java)
            startActivity(intent)
        }, 1000)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        findViewById<TextView>(R.id.text).text = intent?.getStringExtra("anu")
    }
}