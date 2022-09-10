package com.example.internetaccess.core.error_handler

import timber.log.Timber

sealed class GeneralError {
    var errorCode: String = ""
    var exception: Throwable? = null
    var extraData: Any? = null
    var logMessage: String? = null
    var logTag: String = "Solarus Error"
    var logPriority: ErrorLogPriority = ErrorLogPriority.ERROR

    fun logError() {
        if (logMessage != null || exception != null) {
            val message =
                "$logTag / Error code: $errorCode           " +
                        "/ Log message: $logMessage            " +
                        "/ Extra Data: $extraData            "
            Timber.log(logPriority.level, exception, message)
        }
    }


    class E(
        errorCode: String,
        logMessage: String? = null,
        logTag: String? = null,
        exception: Throwable? = null,
        extraData: Any? = null
    ) : GeneralError() {
        init {
            this.logPriority = ErrorLogPriority.ERROR
            this.errorCode = errorCode
            this.logMessage = logMessage
            if (logTag != null) this.logTag = logTag
            this.exception = exception
            this.extraData = extraData
        }

    }

    class W(
        errorCode: String,
        logMessage: String? = null,
        logTag: String? = null,
        exception: Throwable? = null,
        extraData: Any? = null
    ) : GeneralError() {
        init {
            this.logPriority = ErrorLogPriority.WARN
            this.errorCode = errorCode
            this.logMessage = logMessage
            if (logTag != null) this.logTag = logTag
            this.exception = exception
            this.extraData = extraData
        }

    }

    class I(
        errorCode: String,
        logMessage: String? = null,
        logTag: String? = null,
        exception: Throwable? = null,
        extraData: Any? = null
    ) : GeneralError() {
        init {
            this.logPriority = ErrorLogPriority.INFO
            this.errorCode = errorCode
            this.logMessage = logMessage
            if (logTag != null) this.logTag = logTag
            this.exception = exception
            this.extraData = extraData
        }

    }

}