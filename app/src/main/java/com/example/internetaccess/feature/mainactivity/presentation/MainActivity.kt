package com.example.internetaccess.feature.mainactivity.presentation

import android.net.Network
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkStatus
import com.example.internetaccess.core.connectivity.internet_access.ui.HAS_INTERNET_ACCESS
import com.example.internetaccess.core.connectivity.internet_access.ui.InternetAccessDialogFragment
import com.example.internetaccess.core.error_handler.GeneralError
import com.example.internetaccess.core.error_handler.GeneralErrorHandler
import com.example.internetaccess.databinding.ActivityMainBinding
import com.example.internetaccess.feature.second_fragment.data.repository.CorporateRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GeneralErrorHandler, NetworkStatus {

    lateinit var binding: ActivityMainBinding
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }

    @Inject
    lateinit var networkManager: NetworkManager
    private val internetAccessDialogFragment by lazy { InternetAccessDialogFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        observeOnIsNetworkConnected()
    }


    private fun getCurrentFragment() = navHostFragment.childFragmentManager.fragments.firstOrNull()

    override fun handleError(error: GeneralError, callback: GeneralError.() -> Unit) {
        error.logError()
        when (error.errorCode) {
            CorporateRepositoryImpl.CORPORATE_NOT_FOUND -> {
                showToast("Not Found")
            }
        }
        callback(error)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onAvailable(network: Network) {
        (getCurrentFragment() as? NetworkStatus)?.onAvailable(network)
    }

    override fun onLost(network: Network) {
        (getCurrentFragment() as? NetworkStatus)?.onLost(network)
    }

    private fun observeOnIsNetworkConnected() {
        networkManager.isNetworkConnected.observe(this) {
            if (!it) showInternetAccessDialogFragment()
        }
    }

    private fun showInternetAccessDialogFragment() {
        if (internetAccessDialogFragment.isAdded) return
        internetAccessDialogFragment.show(navHostFragment.parentFragmentManager,
            HAS_INTERNET_ACCESS)
    }
}