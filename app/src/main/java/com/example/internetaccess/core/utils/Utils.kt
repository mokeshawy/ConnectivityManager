package com.example.internetaccess.core.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun setLayoutParams(dialog: Dialog) {
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(dialog.window?.attributes)
    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window?.attributes = layoutParams
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}