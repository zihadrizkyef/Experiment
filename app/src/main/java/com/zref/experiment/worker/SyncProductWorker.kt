package com.zref.experiment.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class SyncProductWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i("AOEU", "syncing product ...")
        Thread.sleep(1000)
        return if (Random.nextBoolean()) {
            Log.i("AOEU", "complete syncing product")
            val data = Data.Builder()
                .put("anu", "una")
                .build()
            Result.success(data)
        } else {
            Log.i("AOEU", "failed syncing product")
            Result.failure()
        }
    }
}