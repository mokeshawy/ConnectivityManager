package com.example.internetaccess.feature.mainactivity.presentation

import android.net.Network
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.internetaccess.R
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkStatus
import com.example.internetaccess.core.connectivity.internet_access.internet_access_manager.InternetAccessArrowComponent
import com.example.internetaccess.core.connectivity.internet_access.internet_access_manager.InternetAccessManager
import com.example.internetaccess.core.error_handler.GeneralError
import com.example.internetaccess.core.error_handler.GeneralErrorHandler
import com.example.internetaccess.databinding.ActivityMainBinding
import com.example.internetaccess.feature.second_fragment.data.repository.CorporateRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GeneralErrorHandler, NetworkStatus,
    InternetAccessArrowComponent {

    lateinit var binding: ActivityMainBinding
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()

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

    override fun onInternetAccessAvailable(internetAccess: Boolean) {
        (getCurrentFragment() as? InternetAccessArrowComponent)?.
        onInternetAccessAvailable(internetAccess)
    }

    override fun onInternetAccessUnAvailable(internetAccess: Boolean) {
        (getCurrentFragment() as? InternetAccessArrowComponent)?.
        onInternetAccessUnAvailable(internetAccess)
    }
}