package com.example.internetaccess.core.utils.dialog.dialog_module

import android.app.Activity
import com.example.internetaccess.core.utils.dialog.progress_dialog.ProgressDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DialogModule {

    @Provides
    fun provideSolarusProgressDialog(activity: Activity) = ProgressDialog(activity)
}