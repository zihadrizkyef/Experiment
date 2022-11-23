package com.zref.experiment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zref.experiment.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentDetail : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel by sharedViewModel<AnuViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.itemSelected.observe(viewLifecycleOwner) {
            binding.text.text = it.toString()
        }
    }
}