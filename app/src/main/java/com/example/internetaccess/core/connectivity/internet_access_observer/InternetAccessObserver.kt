package com.example.internetaccess.core.connectivity.internet_access_observer

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.core.connectivity.connectivity_error.ConnectivityError
import com.example.internetaccess.core.connectivity.connectivity_manager.CONNECT_TIME_OUT
import com.example.internetaccess.core.connectivity.connectivity_manager.PING_URL
import com.example.internetaccess.core.connectivity.connectivity_manager.READ_TIME_OUT
import com.example.internetaccess.core.connectivity.connectivity_manager.REQUEST_METHOD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class InternetAccessObserver @Inject constructor(private val activity: Activity) {

    private val internetAccessErrorHandler = (activity as? InternetAccessErrorHandler)

    private val _isInternetAvailable = MutableLiveData(false)
    val isInternetAvailable: LiveData<Boolean> = _isInternetAvailable

    fun getInternetAccessResponse() {
        (activity as AppCompatActivity).lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (isInternetAccess()) {
                    true -> _isInternetAvailable.postValue(true)
                    false -> _isInternetAvailable.postValue(false)
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
        internetAccessErrorHandler?.readInternetAccessExceptionError(error)
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