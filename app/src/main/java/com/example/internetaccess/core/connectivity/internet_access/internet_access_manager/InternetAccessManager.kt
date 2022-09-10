package com.example.internetaccess.core.connectivity.internet_access.internet_access_manager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserver
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserverImpl
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState
import com.example.internetaccess.core.utils.dialog.solarus_progress_dialog.SolarusProgressDialog
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject


class InternetAccessManager @Inject
constructor(private val activity: Activity) {

    var isInternetAccess = false
    private val internetAccessArrowComponent by lazy { activity as InternetAccessArrowComponent }
    private val appCompatActivity = (activity as AppCompatActivity)

    @Inject
    lateinit var internetAccessObserver: InternetAccessObserver

    init {
        registerLifecycle()
    }

    private fun registerLifecycle() {
        appCompatActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                internetAccessObserver = InternetAccessObserverImpl()
                appCompatActivity.lifecycleScope.launchWhenResumed { observeOnInternetAccess() }
            }
        })
    }

    private suspend fun observeOnInternetAccess() {
        internetAccessObserver.observeOnInternetAccess().collect {
            SolarusProgressDialog.hideProgressDialog()
            when (it) {
                InternetAccessState.AVAILABLE -> {
                    isInternetAccess = true
                    internetAccessArrowComponent.onInternetAccessAvailable(true)
                    Timber.e("Internet Access : Available")
                }
                InternetAccessState.LOADING -> SolarusProgressDialog.show(
                    activity, activity.getString(R.string.msg_please_wait)
                )
                InternetAccessState.UNAVAILABLE -> {
                    isInternetAccess = false
                    internetAccessArrowComponent.onInternetAccessUnAvailable(false)
                    Timber.e("Internet Access : UnAvailable")
                }
            }
        }
    }
}