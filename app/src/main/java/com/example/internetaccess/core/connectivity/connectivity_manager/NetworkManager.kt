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
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.core.connectivity.connectivity_error.ConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

const val READ_TIME_OUT = 500
const val CONNECT_TIME_OUT = 5000
const val REQUEST_METHOD = "GET"
const val PING_URL = "https://www.google.com"

class NetworkManager @Inject constructor(private val activity: Activity) {

    var isNetworkConnected = MutableLiveData(false)
    private var networkCapabilities: NetworkCapabilities? = null
    private var getNetworkRequest = getNetworkRequest()
    private var networkCallback = getNetworkCallBack()
    private val appCompatActivity get() = (activity as AppCompatActivity)

    private var readInternetExceptionError: (ConnectivityError) -> Unit = {}

    fun readInternetAccessExceptionError(onInternetExceptionError: (ConnectivityError) -> Unit) {
        this.readInternetExceptionError = onInternetExceptionError
    }

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
                networkCapabilities = getConnectivityManager().getNetworkCapabilities(network)
                checkConnectInternetType()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkConnected.postValue(false)
            }
        }
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

    private fun getInternetAccessResponse() {
        appCompatActivity.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (isInternetAccess()) {
                    true -> isNetworkConnected.postValue(true)
                    false -> isNetworkConnected.postValue(false)
                }
            }
        }
    }


    private fun isInternetAccess(): Boolean {
        try {
            val httpConnection = URL(PING_URL).openConnection() as HttpURLConnection
            httpConnection.apply {
                readTimeout = READ_TIME_OUT
                connectTimeout = CONNECT_TIME_OUT
                requestMethod = REQUEST_METHOD
                connect()
                return responseCode == 200
            }
        } catch (e: SocketTimeoutException) {
            handleInternetExceptionError(getSocketTimeoutExceptionError(e))
        } catch (e: SSLHandshakeException) {
            handleInternetExceptionError(getSSLHandshakeExceptionError(e))
        } catch (e: UnknownHostException) {
            handleInternetExceptionError(getUnknownHostExceptionError(e))
        } catch (e: Exception) {
            handleInternetExceptionError(getGeneralException(e))
        }
        return false
    }

    private fun handleInternetExceptionError(error: ConnectivityError) {
        readInternetExceptionError(error)
    }

    private fun getSocketTimeoutExceptionError(e: SocketTimeoutException) = ConnectivityError.E(
        errorCode = SOCKET_TIME_OUT_EXCEPTION,
        logMessage = "The internet not available in this device",
        exception = e
    )

    private fun getSSLHandshakeExceptionError(e: SSLHandshakeException) = ConnectivityError.E(
        errorCode = SSL_HANDSHAKE_EXCEPTION,
        logMessage = "The internet not available in this device",
        exception = e
    )

    private fun getUnknownHostExceptionError(e: UnknownHostException) = ConnectivityError.E(
        errorCode = UNKNOWN_HOST_EXCEPTION,
        logMessage = "Network is disconnect in this device",
        exception = e
    )

    private fun getGeneralException(e: Exception) = ConnectivityError.E(
        errorCode = GENERAL_EXCEPTION,
        logMessage = "General Exception",
        exception = e
    )

    companion object {
        const val SOCKET_TIME_OUT_EXCEPTION = "SOCKET_TIME_OUT_EXCEPTION"
        const val SSL_HANDSHAKE_EXCEPTION = "SSL_HANDSHAKE_EXCEPTION"
        const val UNKNOWN_HOST_EXCEPTION = "UNKNOWN_HOST_EXCEPTION"
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
    }
}