package com.example.internetaccess.core.connectivity.internet_access_observer

interface InternetAccessErrorHandler {
    fun readInternetAccessExceptionError(errorType: String, exception: Exception) {}
}