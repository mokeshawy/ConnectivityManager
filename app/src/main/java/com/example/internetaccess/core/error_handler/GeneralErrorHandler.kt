package com.example.internetaccess.core.error_handler

import com.example.solarus.core.utils.error.GenralError

interface GeneralErrorHandler {
    fun handleError(error: GenralError, callback: GenralError.() -> Unit = {})
}