package com.example.internetaccess.connectivity.connectivity_manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.connectivity.internet_access_observer.InternetAccessObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val internetAccessObserver: InternetAccessObserver,
) : ConnectivityHelper {

    private var _isNetworkConnected = MutableStateFlow<Boolean>(false)
    override val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected

    private var networkCapabilities: NetworkCapabilities? = null
    private var getNetworkRequest = getNetworkRequest()
    private var networkCallback = getNetworkCallBack()

    init {
        observeAppLifecycle()
    }

    private fun observeAppLifecycle() =
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                getConnectivityManager().registerNetworkCallback(getNetworkRequest, networkCallback)
                handleUnregisteredNetworkState()
                owner.lifecycleScope.launch { observeOnIsInternetAvailable() }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                getConnectivityManager().unregisterNetworkCallback(networkCallback)
            }
        })


    private fun observeOnIsInternetAvailable() {
        internetAccessObserver.isInternetAvailable = {
            _isNetworkConnected.emit(it)
        }
    }

    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder().addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR).addTransportType(TRANSPORT_ETHERNET).build()
    }


    private fun getNetworkCallBack(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                networkCapabilities = getConnectivityManager().getNetworkCapabilities(network)
                checkConnectInternetType()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                getInternetAccessResponse()
            }
        }
    }

    private fun handleUnregisteredNetworkState() {
        if (getActiveNetwork() == null) getInternetAccessResponse()
    }

    private fun getActiveNetwork() =
        getConnectivityManager().getNetworkCapabilities(getConnectivityManager().activeNetwork)


    private fun getConnectivityManager() =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private fun checkConnectInternetType() {
        networkCapabilities?.let {
            when {
                it.hasTransport(TRANSPORT_CELLULAR) -> getInternetAccessResponse()
                it.hasTransport(TRANSPORT_WIFI) -> getInternetAccessResponse()
                it.hasTransport(TRANSPORT_ETHERNET) -> getInternetAccessResponse()
            }
        }
    }


    private fun getInternetAccessResponse() = internetAccessObserver.getInternetAccessResponse()
}