package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listData1 = listOf(
            Data("Zihad", 25),
            Data("Budi", 32),
        )
        val listData2 = listOf(
            Data("Zihad", 25),
            Data("Budi", 32),
        )
        val listData3 = listOf(
            Data("Yusuf", 5),
            Data("Ali", 2),
        )

        val listInt1 = listOf(1, 2)
        val listInt2 = listOf(1, 2)
        val listInt3 = listOf(15, 3)

        Log.i("AOEU", "listdata1 listdata2 = " + (listData1 == listData2))
        Log.i("AOEU", "listdata2 listdata3 = " + (listData2 == listData3))
        Log.i("AOEU", "listint1  listint2  = " + (listInt1 == listInt2))
        Log.i("AOEU", "listint2  listint3  = " + (listInt2 == listInt3))
    }
}