package com.example.internetaccess.feature.mainactivity.presentation

import android.net.Network
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkStatus
import com.example.internetaccess.core.connectivity.internet_access.ui.HAS_INTERNET_ACCESS
import com.example.internetaccess.core.connectivity.internet_access.ui.InternetAccessDialogFragment
import com.example.internetaccess.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NetworkStatus {

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
        internetAccessDialogFragment.show(
            navHostFragment.parentFragmentManager,
            HAS_INTERNET_ACCESS
        )
    }
}