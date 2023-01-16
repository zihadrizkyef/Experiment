package com.zref.experiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.zref.experiment.databinding.ActivityMainBinding
import com.zref.experiment.worker.SyncCustomerWorker
import com.zref.experiment.worker.SyncProductWorker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonHelloWorld.setOnClickListener {
            WorkManager.getInstance(this)
                .beginUniqueWork("ANU", ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(SyncProductWorker::class.java))
                .then(OneTimeWorkRequest.from(SyncCustomerWorker::class.java))
                .enqueue()
        }
    }

}