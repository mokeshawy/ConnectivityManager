package com.example.internetaccess.core.connectivity.internet_access.internet_access_observer

import android.app.Activity
import com.example.internetaccess.core.error_handler.GeneralError
import com.example.internetaccess.feature.mainactivity.presentation.MainActivity
import timber.log.Timber

class InternetAccessErrorHandler {

    fun handleInternetAccessError(activity: Activity ,error: GeneralError) {
        (activity as? MainActivity)?.handleError(error) {
            when (error.errorCode) {

            }
        }
    }
}