package com.example.internetaccess.app_application

import android.app.Application
import android.util.Log
import com.example.internetaccess.BuildConfig

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimberTrees()

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