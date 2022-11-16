package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zref.experiment.databinding.ActivityMainBinding
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tgl = "2022-11-15"
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val date = parser.parse(tgl)


        val date2 = Calendar.getInstance().let {
            it.set(1970, 0, 0, 0, 0)
            it.time
        }
        val calendar = Calendar.getInstance()
        calendar.time = date2

        val date3 = DateTime(1970, 1, 1, 0, 0, 0).toDate()

        Log.i("AOEU", date3.toString())
    }
}