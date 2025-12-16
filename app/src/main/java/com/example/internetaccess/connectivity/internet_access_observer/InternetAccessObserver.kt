package com.example.internetaccess.connectivity.internet_access_observer


import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException


const val READ_TIME_OUT = 500
const val CONNECT_TIME_OUT = 5000
const val REQUEST_METHOD = "GET"
const val PING_URL = "www.google.com"

class InternetAccessObserver @Inject constructor() :
    InternetAccessErrorHandler {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var isInternetAvailable: ( suspend (Boolean) -> Unit)? = null

    fun getInternetAccessResponse() = applicationScope.launch {
        withContext(Dispatchers.IO) {
            isInternetAvailable?.invoke(isInternetAccess())
        }
    }


    private fun isInternetAccess(): Boolean {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                return InetAddress.getByName(PING_URL).isReachable(CONNECT_TIME_OUT)
            }
            val httpConnection = URL("https://$PING_URL").openConnection() as HttpURLConnection
            httpConnection.apply {
                readTimeout = READ_TIME_OUT
                connectTimeout = CONNECT_TIME_OUT
                requestMethod = REQUEST_METHOD
                connect()
                return responseCode == 200
            }
        } catch (e: SocketTimeoutException) {
            readInternetAccessExceptionError(SOCKET_TIME_OUT_EXCEPTION, e)
        } catch (e: SSLHandshakeException) {
            readInternetAccessExceptionError(SSL_HANDSHAKE_EXCEPTION, e)
        } catch (e: UnknownHostException) {
            readInternetAccessExceptionError(UNKNOWN_HOST_EXCEPTION, e)
        } catch (e: Exception) {
            readInternetAccessExceptionError(GENERAL_EXCEPTION, e)
        }
        return false
    }


    override fun readInternetAccessExceptionError(errorType: String, exception: Exception) {}


    companion object {
        const val SOCKET_TIME_OUT_EXCEPTION = "SOCKET_TIME_OUT_EXCEPTION"
        const val SSL_HANDSHAKE_EXCEPTION = "SSL_HANDSHAKE_EXCEPTION"
        const val UNKNOWN_HOST_EXCEPTION = "UNKNOWN_HOST_EXCEPTION"
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
    }
}