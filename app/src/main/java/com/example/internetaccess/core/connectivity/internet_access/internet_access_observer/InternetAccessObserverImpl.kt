package com.example.internetaccess.core.connectivity.internet_access.internet_access_observer

import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.net.InetAddress
import javax.inject.Inject

const val TIME_OUT = 100
const val HOST = "www.manexcard.com"

class InternetAccessObserverImpl @Inject constructor() : InternetAccessObserver {

    override fun observeOnInternetAccess(): Flow<InternetAccessState> {
        return callbackFlow {
            try {
                val internetAccess =
                    withContext(Dispatchers.IO) {
                        InetAddress.getByName(HOST).isReachable(TIME_OUT)
                    }
                launch { send(InternetAccessState.LOADING) }
                delay(500)
                if (internetAccess) launch { send(InternetAccessState.AVAILABLE) }
                if (!internetAccess) launch { send(InternetAccessState.UNAVAILABLE) }
            } catch (e: Exception) {
                // handle exception
            }
            awaitClose { cancel() }
        }.distinctUntilChanged()
    }
}