package com.example.internetaccess.core.utils.dialog.progress_dialog

import android.app.Activity
import android.app.AlertDialog
import androidx.annotation.StringRes
import com.example.internetaccess.core.utils.setLayoutParams
import com.example.internetaccess.databinding.LayoutSolaursProgressDialogBinding
import javax.inject.Inject


class ProgressDialog @Inject constructor(private val activity: Activity) {

    private val binding by lazy { LayoutSolaursProgressDialogBinding.inflate(activity.layoutInflater) }
    private var dialog: AlertDialog? = null

    init {
        buildAlertDialogView()
    }

    private fun buildAlertDialogView(): AlertDialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        dialog = builder.create()
        dialog?.let { setLayoutParams(it) }
        dialog?.setCancelable(false)
        return dialog
    }

    fun show(@StringRes message: Int) {
        binding.messageTv.text = activity.resources.getString(message)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}