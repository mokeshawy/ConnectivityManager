package com.example.internetaccess.feature.second_fragment.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.internetaccess.core.base_fragment.BaseFragment
import com.example.internetaccess.databinding.FragmentSecondBinding
import com.example.internetaccess.feature.second_fragment.domain.viewmodel.SecondFragmentViewModel

class SecondFragment : BaseFragment<FragmentSecondBinding, SecondFragmentViewModel>() {

    override val binding by lazy { FragmentSecondBinding.inflate(layoutInflater) }
    override val viewModel: SecondFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}