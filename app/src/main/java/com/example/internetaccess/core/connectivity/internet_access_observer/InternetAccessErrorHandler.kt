package com.example.internetaccess.core.connectivity.internet_access_observer

import com.example.internetaccess.core.connectivity.connectivity_error.ConnectivityError

interface InternetAccessErrorHandler {
    fun readInternetAccessExceptionError(error: ConnectivityError) {}
}