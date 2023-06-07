package com.zref.experiment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.zref.experiment.databinding.FragmentServiceBinding

class FragmentService : Fragment() {
    private lateinit var binding: FragmentServiceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as MainActivity).viewModel

        binding.buttonSideToggle.setOnClickListener {
            if (binding.root.targetPosition == 1F) {
                viewModel.isExpand.value = true
                binding.root.transitionToStart()
            } else {
                viewModel.isExpand.value = false
                binding.root.transitionToEnd()
            }
        }

        binding.root.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                Log.i("AOEU", "onTransitionStarted")
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                Log.i("AOEU", "progress -> $progress")
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.i("AOEU", "onTransitionCompleted")
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                Log.i("AOEU", "onTransitionTrigger")
            }

        })

    }
}