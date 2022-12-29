package com.example.internetaccess.feature.mainactivity.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_error.ConnectivityError
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessErrorHandler
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.GENERAL_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.SOCKET_TIME_OUT_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.SSL_HANDSHAKE_EXCEPTION
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver.Companion.UNKNOWN_HOST_EXCEPTION
import com.example.internetaccess.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), InternetAccessErrorHandler {

    private lateinit var binding: ActivityMainBinding

    // get network manager
    @Inject
    lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        hideActionBar()
        // register network manager lifecycle
        networkManager.registerNetworkManagerLifecycle()
        observeOnIsNetworkConnected()
    }

    private fun hideActionBar() = supportActionBar?.hide()

    // Observe on network connected
    private fun observeOnIsNetworkConnected() {
        networkManager.isNetworkConnectedLiveData.observeForever {
            binding.networkStatusTv.text = if (it) {
                resources.getString(R.string.internetIsAvailable)
            } else {
                resources.getString(R.string.internetIsNotAvailable)
            }
        }
    }


    // If you need handle internet access exception { Implement InternetAccessErrorHandler }
    // And check on error code such as this example
    override fun readInternetAccessExceptionError(error: ConnectivityError) {
        lifecycleScope.launchWhenResumed {
            when (error.errorCode) {
                SOCKET_TIME_OUT_EXCEPTION -> showShortToast(SOCKET_TIME_OUT_EXCEPTION)
                SSL_HANDSHAKE_EXCEPTION -> showShortToast(SSL_HANDSHAKE_EXCEPTION)
                UNKNOWN_HOST_EXCEPTION -> showShortToast(UNKNOWN_HOST_EXCEPTION)
                GENERAL_EXCEPTION -> showShortToast(GENERAL_EXCEPTION)
            }
        }
    }

    private fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}