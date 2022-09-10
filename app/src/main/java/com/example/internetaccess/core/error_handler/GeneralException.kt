package com.example.solarus.core.utils.error

import com.example.internetaccess.core.error_handler.GeneralError

data class GeneralException(val generalError: GeneralError) : Exception()