package com.example.internetaccess.feature.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.internetaccess.R
import com.example.internetaccess.databinding.ActivityMainBinding
import com.example.solarus.core.utils.error.GeneralErrorHandler
import com.example.solarus.core.utils.error.GenralError

class MainActivity : AppCompatActivity(),GeneralErrorHandler {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

    override fun handleError(error: GenralError, callback: GenralError.() -> Unit) {
       error.logError()

        callback(error)
    }
}