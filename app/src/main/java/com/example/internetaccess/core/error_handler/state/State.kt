package com.example.internetaccess.core.error_handler.state

import com.example.internetaccess.core.error_handler.GeneralError

sealed class State<T> {
    var hasBeenHandled = false

    class Initial<T> : State<T>()
    class Loading<T> : State<T>()
    data class Success<T>(val data: T?) : State<T>()
    data class Error<T>(val error: GeneralError) : State<T>()
}