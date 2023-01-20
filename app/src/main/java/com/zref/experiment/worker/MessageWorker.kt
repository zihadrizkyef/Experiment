package com.zref.experiment.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class MessageWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("message")
        Log.i("AOEU", "$message start")
        Thread.sleep(Random.nextLong(1000, 5000))

        return if (Random.nextBoolean()) {
            Log.i("AOEU", "$message done : SUCCESS")
            Result.success()
        } else {
            Log.i("AOEU", "$message done : FAILED")
            Result.failure()
        }
    }
}