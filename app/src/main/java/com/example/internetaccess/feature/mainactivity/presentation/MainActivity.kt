package com.example.internetaccess.feature.mainactivity.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_error.ConnectivityError
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager.Companion.GENERAL_EXCEPTION
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager.Companion.SOCKET_TIME_OUT_EXCEPTION
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager.Companion.SSL_HANDSHAKE_EXCEPTION
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager.Companion.UNKNOWN_HOST_EXCEPTION
import com.example.internetaccess.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        hideActionBar()
        networkManager.readInternetAccessExceptionError(::handleInternetAccessError)
        observeOnIsNetworkConnected()
    }

    private fun hideActionBar() = supportActionBar?.hide()

    private fun observeOnIsNetworkConnected() {
        networkManager.isNetworkConnected.observe(this) {
            binding.networkStatusTv.text = if (it) {
                resources.getString(R.string.internetIsAvailable)
            } else {
                resources.getString(R.string.internetIsNotAvailable)
            }
        }
    }

    private fun handleInternetAccessError(connectivityError: ConnectivityError) {
        when (connectivityError.errorCode) {
            SOCKET_TIME_OUT_EXCEPTION -> Timber.e(SOCKET_TIME_OUT_EXCEPTION)
            SSL_HANDSHAKE_EXCEPTION -> Timber.e(SSL_HANDSHAKE_EXCEPTION)
            UNKNOWN_HOST_EXCEPTION -> Timber.e(UNKNOWN_HOST_EXCEPTION)
            GENERAL_EXCEPTION -> Timber.e(GENERAL_EXCEPTION)
        }
    }
}