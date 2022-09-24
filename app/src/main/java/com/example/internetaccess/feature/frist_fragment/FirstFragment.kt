package com.example.internetaccess.feature.frist_fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.internetaccess.R
import com.example.internetaccess.core.base_fragment.BaseFragment
import com.example.internetaccess.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding, FirstFragmentViewModel>(){

    override val binding by lazy { FragmentFirstBinding.inflate(layoutInflater) }
    override val viewModel: FirstFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.startNextScreen.setOnClickListener {
            if (isInternetAccess()){
                showToast("Available")
            }else{
                showToast("UnAvailable")
            }
        }
    }


    fun isInternetAccess(): Boolean {
        try {
            val url = URL("https://web.manexcard.com")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.apply {
                readTimeout = 30000
                connectTimeout = 3500
                requestMethod = "GET"
                connect()
                if (responseCode == 200) {
                    return true
                }
            }
        } catch (e: Exception) {

        }
        return false
    }

    private fun startCountDownTimer() {
      lifecycleScope.launch {
            for (currentSecond in 59 downTo 0) {
                Timber.e("CountDownTimer : $currentSecond")
                delay(1000)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }
}