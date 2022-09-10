package com.example.internetaccess.feature.second_fragment.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.internetaccess.R
import com.example.internetaccess.core.base_fragment.BaseFragment
import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.core.utils.dialog.solarus_progress_dialog.SolarusProgressDialog
import com.example.internetaccess.databinding.FragmentSecondBinding
import com.example.internetaccess.feature.second_fragment.data.model.reponse.corporate_response.CorporateResponseDto
import com.example.internetaccess.feature.second_fragment.domain.viewmodel.SecondFragmentViewModel

class SecondFragment : BaseFragment<FragmentSecondBinding, SecondFragmentViewModel>() {

    override val binding by lazy { FragmentSecondBinding.inflate(layoutInflater) }
    override val viewModel: SecondFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private suspend fun observeOnCorporateResponse() {
        viewModel.responseState.collect {
            SolarusProgressDialog.hideProgressDialog()
            when (it) {
                is State.Error -> showToast("${it.error}")
                is State.Initial -> {}
                is State.Loading -> SolarusProgressDialog.show(
                    requireActivity(),
                    getString(R.string.msg_please_wait)
                )
                is State.Success -> it.data?.let { it1 -> setUpView(it1) }
            }
        }
    }

    private fun setUpView(item: CorporateResponseDto) {
        binding.corporateId.text = String.format("Corporate ID : %d", item.id)
        binding.corporateName.text = String.format("Corporate Name : %s", item.enName)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), "$message", Toast.LENGTH_SHORT).show()
    }
}