package com.zref.experiment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.zref.experiment.databinding.ActivityMainBinding
import com.zref.experiment.worker.MessageWorker
import com.zref.experiment.worker.SyncCustomerWorker
import com.zref.experiment.worker.SyncProductWorker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            WorkManager.getInstance(this)
                .beginUniqueWork(
                    "ANU",
                    ExistingWorkPolicy.REPLACE,
                    listOf(createWorker("1"), createWorker("2"), createWorker("3"))
                )
                .then(
                    listOf(createWorker("11"), createWorker("12"), createWorker("13"))
                )
                .enqueue()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun createWorker(message: String): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<MessageWorker>()
            .setInputData(Data.Builder().put("message", message).build())
            .build()
    }

}