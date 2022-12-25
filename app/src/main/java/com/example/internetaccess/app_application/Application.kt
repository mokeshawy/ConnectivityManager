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
        plantTimberTrees()

    }

    private fun plantTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}