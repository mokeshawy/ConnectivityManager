package com.example.internetaccess.app_application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.internetaccess.BuildConfig
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifeCycleCallback()
        plantTimberTrees()

    }

    private fun registerActivityLifeCycleCallback() {

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // no implemented any think here

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                // no implemented any think here
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                // no implemented any think here
            }

            override fun onActivityDestroyed(activity: Activity) {
                // no implemented any think here
            }

        })
    }


    private fun showToast(activity: Activity, message: String) {
        Toast.makeText(activity, "$message", Toast.LENGTH_SHORT).show()
    }

    private fun plantTimberTrees() {
        Timber.plant(CrashReportingTree())
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private inner class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.log(message)
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) return
            if (t != null) {
                crashlytics.recordException(t)
            } else {
                crashlytics.recordException(RuntimeException("Internet Access Error  application: "))
            }
        }

    }
}