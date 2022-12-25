package com.example.internetaccess.core.connectivity.internet_access.internet_access_observer

import android.app.Activity
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.AVAILABLE
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.UNAVAILABLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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

class InternetAccessObserver @Inject constructor(private val activity: Activity) {

    fun getInternetAccessResponse(): Flow<InternetAccessState> {
        return callbackFlow {
            withContext(Dispatchers.IO) {
                when (isInternetAccess()) {
                    true -> send(AVAILABLE)
                    false -> send(UNAVAILABLE)
                }
            }
            awaitClose { cancel() }
        }.distinctUntilChanged()
    }

    private fun isInternetAccess(): Boolean {
        try {
            val httpConnection =
                URL("https://www.google.com").openConnection() as HttpURLConnection
            httpConnection.apply {
                readTimeout = READ_TIME_OUT
                connectTimeout = CONNECT_TIME_OUT
                requestMethod = REQUEST_METHOD
                connect()
                return responseCode == 200
            }
        } catch (e: SocketTimeoutException) {
            //SOCKET_TIME_OUT_EXCEPTION
        } catch (e: SSLHandshakeException) {
            //SSL_HANDSHAKE_EXCEPTION
        } catch (e: UnknownHostException) {
            //UNKNOWN_HOST_EXCEPTION
        } catch (e: Exception) {
            //GENERAL_EXCEPTION
        }
        return false
    }


    companion object {
        const val SOCKET_TIME_OUT_EXCEPTION = "SOCKET_TIME_OUT_EXCEPTION"
        const val SSL_HANDSHAKE_EXCEPTION = "SSL_HANDSHAKE_EXCEPTION"
        const val UNKNOWN_HOST_EXCEPTION = "UNKNOWN_HOST_EXCEPTION"
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
    }
}