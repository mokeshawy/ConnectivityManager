package com.example.internetaccess.core.error_handler

interface GeneralErrorHandler {
    fun handleError(error: GeneralError, callback: GeneralError.() -> Unit = {})
}