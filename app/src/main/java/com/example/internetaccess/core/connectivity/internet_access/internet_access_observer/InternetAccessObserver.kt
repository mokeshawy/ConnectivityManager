package com.example.internetaccess.core.connectivity.internet_access.internet_access_observer

import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.AVAILABLE
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.UNAVAILABLE
import com.example.internetaccess.core.error_handler.GeneralError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

class InternetAccessObserver {

    private var onInternetExceptionError: (GeneralError) -> Unit = {}

    fun readInternetAccessExceptionError(onInternetExceptionError: (GeneralError) -> Unit) {
        this.onInternetExceptionError = onInternetExceptionError
    }


    fun getObserveOnIntentAccess(): Flow<InternetAccessState> {
        return callbackFlow {
            withContext(Dispatchers.IO) {
                when (isInternetAccess()) {
                    true -> launch { send(AVAILABLE) }
                    false -> launch { send(UNAVAILABLE) }
                }
            }
            awaitClose { cancel() }
        }.distinctUntilChanged()
    }

    private fun isInternetAccess(): Boolean {
        try {
            val httpURLConnection =
                URL("https://web.manexcard.com").openConnection() as HttpURLConnection
            httpURLConnection.readTimeout = 145
            httpURLConnection.connectTimeout = 2000
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.connect()
            return httpURLConnection.responseCode == 200
        } catch (e:SocketTimeoutException) {
            handleInternetExceptionError(getSocketTimeoutExceptionError(e))
        }catch (e:UnknownHostException){
            handleInternetExceptionError(getUnknownHostExceptionError(e))
        }catch (e: Exception){
            handleInternetExceptionError(getGeneralException(e))
        }
        return false
    }

    private fun handleInternetExceptionError(error: GeneralError) {
        onInternetExceptionError(error)
    }

    private fun getSocketTimeoutExceptionError(e: SocketTimeoutException) = GeneralError.I(
        errorCode = SOCKET_TIME_OUT_EXCEPTION,
        logMessage = "The internet not available in this device",
        extraData = e
    )

    private fun getUnknownHostExceptionError(e: UnknownHostException) = GeneralError.I(
        errorCode = UNKNOWN_HOST_EXCEPTION,
        logMessage = "Network is disconnect in this device",
        extraData = e
    )

    private fun getGeneralException(e: Exception) = GeneralError.E(
        errorCode = GENERAL_EXCEPTION,
        logMessage = "General Exception",
        extraData = e
    )

    companion object {
        const val SOCKET_TIME_OUT_EXCEPTION = "SOCKET_TIME_OUT_EXCEPTION"
        const val UNKNOWN_HOST_EXCEPTION = "UNKNOWN_HOST_EXCEPTION"
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
    }
}