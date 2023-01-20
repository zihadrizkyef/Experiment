package com.zref.experiment.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncCustomerWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val anu = inputData.getString("anu")
        Log.i("AOEU", "syncing customer ...$anu")
        Thread.sleep(1000)
        Log.i("AOEU", "complete syncing customer")

        return Result.success()
    }
}