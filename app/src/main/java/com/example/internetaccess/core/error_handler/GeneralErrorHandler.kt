package com.example.solarus.core.utils.error

interface GeneralErrorHandler {
    fun handleError(error: GenralError, callback: GenralError.() -> Unit = {})
}