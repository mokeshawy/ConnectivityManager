package com.example.internetaccess.feature.mainactivity.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_manager.ConnectivityManager
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessErrorHandler
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.GENERAL_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.SOCKET_TIME_OUT_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.SSL_HANDSHAKE_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.UNKNOWN_HOST_EXCEPTION
import com.example.internetaccess.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), InternetAccessErrorHandler {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        hideActionBar()
        observeOnIsNetworkConnected()
    }

    private fun hideActionBar() = supportActionBar?.hide()

    private fun observeOnIsNetworkConnected() {
        connectivityManager.isNetworkConnected.observe(this) {
            binding.networkStatusTv.text = if (it) {
                resources.getString(R.string.internetIsAvailable)
            } else {
                resources.getString(R.string.internetIsNotAvailable)
            }
        }
    }

    // If you need handle internet access exception error { Implement InternetAccessErrorHandler }
    // And check on error code such as this example
    override fun readInternetAccessExceptionError(errorType: String, exception: Exception) {
        lifecycleScope.launch {
            when (errorType) {
                SOCKET_TIME_OUT_EXCEPTION -> showShortToast("$exception")
                SSL_HANDSHAKE_EXCEPTION -> showShortToast("$exception")
                UNKNOWN_HOST_EXCEPTION -> showShortToast("$exception")
                GENERAL_EXCEPTION -> showShortToast("$exception")
            }
        }
    }

    private fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}