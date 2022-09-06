package com.example.internetaccess.feature.mainactivity.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.R
import com.example.internetaccess.core.error_handler.GeneralErrorHandler
import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.core.utils.dialog.solarus_progress_dialog.SolarusProgressDialog
import com.example.internetaccess.databinding.ActivityMainBinding
import com.example.internetaccess.feature.mainactivity.data.model.reponse.corporate_response.CorporateResponseDto
import com.example.internetaccess.feature.mainactivity.data.repository.CorporateRepositoryImpl
import com.example.internetaccess.feature.mainactivity.domain.viewmodel.ActivityMainViewModel
import com.example.solarus.core.utils.error.GenralError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GeneralErrorHandler {

    lateinit var binding: ActivityMainBinding
    private val viewModel: ActivityMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        observeResponse()

        binding.start.setOnClickListener {
            viewModel.getCorporate(4)
        }

    }

    private fun observeResponse() {
        lifecycleScope.launchWhenCreated {
            viewModel.responseState.collect {
                SolarusProgressDialog.hideProgressDialog()
                when (it) {
                    is State.Error -> Toast.makeText(this@MainActivity,
                        "${it.error}",
                        Toast.LENGTH_SHORT).show()
                    is State.Initial -> {}
                    is State.Loading -> SolarusProgressDialog.show(this@MainActivity,
                        getString(R.string.msg_please_wait))
                    is State.Success -> it.data?.let { it1 -> setUpView(it1) }
                }
            }
        }
    }

    private fun setUpView(item: CorporateResponseDto) {
        binding.corporateId.text = String.format("Corporate ID : %d", item.id)
        binding.corporateName.text = String.format("Corporate Name : %s", item.enName)
    }

    override fun handleError(error: GenralError, callback: GenralError.() -> Unit) {
        error.logError()
        when (error.errorCode) {
            CorporateRepositoryImpl.CORPORATE_NOT_FOUND -> {
                Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show()
            }
        }
        callback(error)
    }
}