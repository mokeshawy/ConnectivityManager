package com.example.internetaccess.core.utils.dialog.solarus_progress_dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.example.internetaccess.R
import com.example.internetaccess.databinding.LayoutSolaursProgressDialogBinding


object SolarusProgressDialog {


    lateinit var binding: LayoutSolaursProgressDialogBinding
    var dialog: CustomDialog? = null

    fun show(activity: Activity, title: CharSequence?): Dialog? {

        val inflater = activity.layoutInflater
        binding = LayoutSolaursProgressDialogBinding.inflate(inflater)

        title?.let {
            binding.messageTv.text = it
        }
        binding.nfcScanAnim.setAnimation(R.raw.loading_anim)

        dialog = CustomDialog(activity)
        dialog?.setContentView(binding.root)

        try {
            dialog?.show()
        } catch (e: Exception) {
        }

        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            window?.decorView?.rootView?.setBackgroundResource(R.color.dialogBackground)
        }
    }

    fun hideProgressDialog() {
        dialog?.dismiss()
    }
}