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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.zref.experiment.databinding.CustomerDisplayBinding

class CustomerDisplayPresentation(
    context: Context,
    display: Display,
) : Presentation(context, display) {

    private lateinit var binding: CustomerDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.updateLayoutParams<ViewGroup.LayoutParams> {
            width = display.width
            height = display.height
        }
    }

    fun setVideoUri(uri: Uri) {
        Log.i("AOEU", "uri display $uri")
        try {
            binding.textView.isVisible = false
            binding.videoView.isVisible = true
            binding.videoView.setVideoURI(uri)
            binding.videoView.start()
            binding.videoView.setOnPreparedListener {
                it.isLooping = true
                val scaleX = display.width.toFloat() / it.videoWidth.toFloat()
                val scaleY = display.height.toFloat() / it.videoHeight.toFloat()

                if (scaleX > scaleY) {
                    binding.videoView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        width = (it.videoWidth * scaleX).toInt()
                        height = (it.videoHeight * scaleX).toInt()
                    }
                } else {
                    binding.videoView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        width = (it.videoWidth * scaleY).toInt()
                        height = (it.videoHeight * scaleY).toInt()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.textView.isVisible = true
            binding.videoView.isVisible = false
        }
    }
}