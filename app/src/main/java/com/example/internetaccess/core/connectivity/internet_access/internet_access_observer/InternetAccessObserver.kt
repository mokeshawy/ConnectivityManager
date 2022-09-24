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
import java.net.URL

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
        } catch (e: Exception) {
            handleInternetExceptionError(getGeneralException(e))
        }
        return false
    }

    private fun handleInternetExceptionError(error: GeneralError) {
        onInternetExceptionError(error)
    }

    private fun getGeneralException(e: Exception) = GeneralError.E(
        errorCode = GENERAL_EXCEPTION,
        logMessage = "Error with connect host name with general Exception",
        extraData = e
    )

    companion object {
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
    }
}