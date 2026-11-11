package com.example.internetaccess.connectivity.connectivity_manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.internetaccess.connectivity.internet_access_observer.InternetAccessObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ConnectivityManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val internetAccessObserver: InternetAccessObserver,
) : ConnectivityHelper {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isNetworkConnected =
        MutableStateFlow(value = isCurrentlyConnected())

    override val isNetworkConnected: StateFlow<Boolean> =
        _isNetworkConnected.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            validateInternet()
        }

        override fun onLost(network: Network) {
            _isNetworkConnected.value = false
        }

        override fun onUnavailable() {
            _isNetworkConnected.value = false
        }
    }

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            _isNetworkConnected.value = isCurrentlyConnected()
            validateInternet()
        }
    }

    init {
        connectivityManager.registerNetworkCallback(buildNetworkRequest(), networkCallback)
        observeInternetValidation()
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    private fun observeInternetValidation() {
        internetAccessObserver.isInternetAvailable = { isAvailable ->
            _isNetworkConnected.value = isAvailable
        }
    }

    private fun validateInternet() {
        internetAccessObserver.getInternetAccessResponse()
    }

    private fun isCurrentlyConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun buildNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

}