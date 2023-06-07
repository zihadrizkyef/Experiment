package com.zref.experiment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zref.experiment.databinding.FragmentNotificationBinding
import com.zref.experiment.databinding.FragmentServiceBinding

class FragmentNotification : Fragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = ItemAdapter()

        val viewModel = (activity as MainActivity).viewModel
        viewModel.isExpand.observe(viewLifecycleOwner) {
            if (it) {
                binding.root.transitionToStart()
            } else {
                binding.root.transitionToEnd()
            }
        }
    }
}