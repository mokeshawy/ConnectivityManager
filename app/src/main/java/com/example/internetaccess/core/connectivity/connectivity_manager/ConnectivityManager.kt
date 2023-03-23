package com.example.internetaccess.core.connectivity.connectivity_manager

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver
import javax.inject.Inject

class ConnectivityManager @Inject constructor(
    private val activity: Activity, private val internetAccessObserver: InternetAccessObserver
) {

    private var _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> = _isNetworkConnected

    private var isNetworkRegister: Boolean = false
    private var networkCapabilities: NetworkCapabilities? = null
    private var getNetworkRequest = getNetworkRequest()
    private var networkCallback = getNetworkCallBack()
    private val appCompatActivity get() = (activity as AppCompatActivity)


    init {
        handleNetworkCallbackRegistration()
    }

    private fun handleNetworkCallbackRegistration() {
        appCompatActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                handleUnregisteredNetworkState()
                getConnectivityManager().registerNetworkCallback(getNetworkRequest, networkCallback)
                observeOnIsInternetAvailable()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                getConnectivityManager().unregisterNetworkCallback(networkCallback)
            }
        })
    }


    private fun observeOnIsInternetAvailable() {
        internetAccessObserver.isInternetAvailable.observe(appCompatActivity, Observer {
            _isNetworkConnected.postValue(it)
        })
    }

    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder().addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR).addTransportType(TRANSPORT_ETHERNET).build()
    }


    private fun getNetworkCallBack(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkRegister = true
                networkCapabilities = getConnectivityManager().getNetworkCapabilities(network)
                checkConnectInternetType()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkRegister = true
                getInternetAccessResponse()
            }
        }
    }

    private fun handleUnregisteredNetworkState() {
        if (getActiveNetwork() == null) getInternetAccessResponse()
    }

    private fun getActiveNetwork() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getConnectivityManager().getNetworkCapabilities(getConnectivityManager().activeNetwork)
    } else {
        null
    }


    private fun getConnectivityManager() =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


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