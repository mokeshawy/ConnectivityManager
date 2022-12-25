package com.example.internetaccess.core.base_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkStatus
import com.example.internetaccess.core.utils.showToast
import javax.inject.Inject

abstract class BaseFragment<dataBinding : ViewDataBinding, viewModel : ViewModel> :
    Fragment(), NetworkStatus {

    protected abstract val viewModel: viewModel
    abstract val binding: dataBinding

    @Inject
    lateinit var networkManager: NetworkManager

    protected val isNetworkAvailable get() = networkManager.isAvailable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = binding.root
        return view
    }

    fun Int.localize() = getString(this)

    fun Int.localize(vararg args: Any) = getString(this, *args)

    fun Int.showShortToast() {
        requireActivity().showToast(localize(), Toast.LENGTH_SHORT)
    }

    fun showShortToast(message: String) {
        requireActivity().showToast(message, Toast.LENGTH_SHORT)
    }

    fun Int.showLongToast() {
        showLongToast(localize())
    }

    fun showLongToast(message: String) {
        requireActivity().showToast(message)
    }

    fun getStartNewActivity(context: Context, activity: Activity) {
        startActivity(Intent(context, activity::class.java))
    }

    fun setActionBarLayout(@LayoutRes layout: Int) {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            setDisplayShowCustomEnabled(true)
            setCustomView(layout)
        }
    }
    

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onAvailable(network: Network) {
        //Not yet implemented
    }

    override fun onLost(network: Network) {
        //showShortToast("Network not available")
    }
}