package com.example.internetaccess.core.connectivity.internet_access_observer

interface InternetAccessErrorHandler {
    fun readInternetAccessExceptionError(errorCode: String,exception: Exception) {}
}