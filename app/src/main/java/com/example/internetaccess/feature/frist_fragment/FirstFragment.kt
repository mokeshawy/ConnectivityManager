package com.example.internetaccess.feature.frist_fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.internetaccess.core.base_fragment.BaseFragment
import com.example.internetaccess.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding, FirstFragmentViewModel>() {

    override val binding by lazy { FragmentFirstBinding.inflate(layoutInflater) }
    override val viewModel: FirstFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }
}