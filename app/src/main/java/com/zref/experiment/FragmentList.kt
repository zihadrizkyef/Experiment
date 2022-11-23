package com.zref.experiment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zref.experiment.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentList : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel by sharedViewModel<AnuViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button1.setOnClickListener {
            viewModel.itemSelected.value = 1
        }
        binding.button2.setOnClickListener {
            viewModel.itemSelected.value = 2
        }
        binding.button3.setOnClickListener {
            viewModel.itemSelected.value = 3
        }
    }
}