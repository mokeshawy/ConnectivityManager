package com.example.internetaccess.core.connectivity.connectivity_manager

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import javax.inject.Inject

class NetworkManager @Inject constructor(private val activity: Activity) {

    var isAvailable = false
    var isNetworkConnected = MutableLiveData(false)
    private var networkCapabilities: NetworkCapabilities? = null
    private var getNetworkRequest = getNetworkRequest()
    private var networkCallback = getNetworkCallBack()
    private val appCompatActivity get() = (activity as AppCompatActivity)
    private val networkStatus by lazy { activity as NetworkStatus }


    init {
        registerLifecycle()
    }

    private fun registerLifecycle() {
        appCompatActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                getConnectivityManager().registerNetworkCallback(getNetworkRequest, networkCallback)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                getConnectivityManager().unregisterNetworkCallback(networkCallback)
            }
        })
    }


    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR)
            .addTransportType(TRANSPORT_ETHERNET)
            .build()
    }


    private fun getNetworkCallBack(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {


            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isAvailable = true
                networkStatus.onAvailable(network)
                networkCapabilities = getConnectivityManager().getNetworkCapabilities(network)
                checkConnectInternetType()
            }


            override fun onLost(network: Network) {
                super.onLost(network)
                isAvailable = false
                isNetworkConnected.postValue(false)
                networkStatus.onLost(network)
                checkDisconnectInternetType()
            }
        }
    }

    private fun getConnectivityManager() =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private fun checkConnectInternetType() {
        networkCapabilities?.let {
            when {
                it.hasTransport(TRANSPORT_CELLULAR) -> {
                    isNetworkConnected.postValue(true)
                    Timber.e("Cellular is on")
                    //cellular turn on
                }
                it.hasTransport(TRANSPORT_WIFI) -> {
                    isNetworkConnected.postValue(true)
                    Timber.e("Wifi is on")
                    //wifi turn on
                }
                it.hasTransport(TRANSPORT_ETHERNET) -> {
                    isNetworkConnected.postValue(true)
                    Timber.e("Ethernet is on")
                    //ether net turn on
                }
            }
        }
    }

    private fun checkDisconnectInternetType() {
        networkCapabilities?.let {
            when {
                it.hasTransport(TRANSPORT_CELLULAR) -> {
                    //cellular turn off
                }
                it.hasTransport(TRANSPORT_WIFI) -> {
                    //wifi turn off
                }
                it.hasTransport(TRANSPORT_ETHERNET) -> {
                    //here ether net turn off
                }
            }
        }
    }
}