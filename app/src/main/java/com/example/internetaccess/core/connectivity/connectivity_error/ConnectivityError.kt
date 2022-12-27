package com.example.internetaccess.core.connectivity.connectivity_error

sealed class ConnectivityError {
    var errorCode: String = ""
    var logMessage: String? = null
    var exception: Throwable? = null

    class E(
        errorCode: String,
        logMessage: String,
        exception: Throwable
    ) : ConnectivityError() {
        init {
            this.errorCode = errorCode
            this.logMessage = logMessage
            this.exception = exception
        }
    }
}