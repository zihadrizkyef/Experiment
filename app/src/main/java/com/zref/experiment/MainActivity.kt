package com.zref.experiment

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.zref.experiment.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customerDisplay: CustomerDisplayPresentation
    private val videoResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            Log.i("AOEU", "uri result $it")
            val source = contentResolver.openInputStream(it)!!
            val rootDir = getDir("", Context.MODE_PRIVATE)
            val fileDir = File(rootDir, "video")
            fileDir.mkdirs()
            val file = File(fileDir, "customer-display.mp4")
            val out = FileOutputStream(file)

            val fileLength = contentResolver.openAssetFileDescriptor(it, "r")!!.use { it.length }
            val buf = ByteArray(512)
            var len: Int
            var total = 0L
            while (source.read(buf).also { len = it } != -1) {
                total += len.toLong()
                val progress = (total * 100 / fileLength).toInt()
                Log.i("AOEU", "progress $progress")
                out.write(buf, 0, len)
            }

            source.close()
            out.close()
            customerDisplay.setVideoUri(file.toUri())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            videoResultCallback.launch("video/*")
        }

        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val secondDisplay = displayManager.displays[1]
        customerDisplay = CustomerDisplayPresentation(this, secondDisplay)
        customerDisplay.show()

        val rootDir = getDir("", Context.MODE_PRIVATE)
        val fileDir = File(rootDir, "video")
        val file = File(fileDir, "customer-display.mp4")
        if (file.exists()) {
            customerDisplay.setVideoUri(file.toUri())
        }
    }
}