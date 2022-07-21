package com.zref.experiment

import android.app.Presentation
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.RelativeLayout
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import com.zref.experiment.R
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.updateLayoutParams
import com.zref.experiment.databinding.CustomerDisplayBinding

class CustomerDisplayPresentation(
    context: Context,
    display: Display,
) : Presentation(context, display) {

    private lateinit var binding: CustomerDisplayBinding
    private var mediaPlayer = MediaPlayer()
    private var surfaceHolder: SurfaceHolder? = null
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.updateLayoutParams<ViewGroup.LayoutParams> {
            width = display.width
            height = display.height
        }

        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceCreated(holder: SurfaceHolder) {
                surfaceHolder = holder
                if (videoUri != null) {
                    mediaPlayer.reset()
                    mediaPlayer.setDisplay(surfaceHolder)
                    if (videoUri != null) {
                        mediaPlayer.setDataSource(context, videoUri!!)
                        mediaPlayer.isLooping = true
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }
                }
            }
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                surfaceHolder = null
            }
        })
    }

    fun setVideoUri(uri: Uri) {
        videoUri = uri
        if (surfaceHolder != null) {
            mediaPlayer.reset()
            mediaPlayer.setDisplay(surfaceHolder)
        }
        mediaPlayer.setDataSource(context, uri)
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}