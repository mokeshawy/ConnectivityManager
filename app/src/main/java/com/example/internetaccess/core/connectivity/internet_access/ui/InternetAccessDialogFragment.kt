package com.example.internetaccess.core.connectivity.internet_access.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessErrorHandler
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserver
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.AVAILABLE
import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState.UNAVAILABLE
import com.example.internetaccess.core.error_handler.GeneralError
import com.example.internetaccess.databinding.FragmentDialogInternetAccessBinding
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

const val HAS_INTERNET_ACCESS = "HAS_INTERNET_ACCESS"

@AndroidEntryPoint
class InternetAccessDialogFragment : DialogFragment() {

    lateinit var binding: FragmentDialogInternetAccessBinding
    private val internetAccessObserver by lazy { InternetAccessObserver() }
    private val hasInternetAccessTag
        get() = parentFragmentManager.findFragmentByTag(HAS_INTERNET_ACCESS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDialogInternetAccessBinding.inflate(layoutInflater)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        internetAccessObserver.readInternetAccessExceptionError(::handleInternetAccessExceptionError)
        if (hasInternetAccessTag != null) {
            startCountDownTimer()
        }
    }

    override fun onStart() {
        super.onStart()
        setDialogView()

    }

    private fun handleInternetAccessExceptionError(error: GeneralError) {
        InternetAccessErrorHandler().handleInternetAccessError(requireActivity(), error)
    }

    private fun setDialogView() {
        val dialog = dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.setLayout(width, height)
    }

    private fun startCountDownTimer() {
        lifecycleScope.launch {
            for (currentSecond in 59 downTo 0) {
                Timber.e("$currentSecond")
                binding.countTimerTv.text = String.format("Can be restart OTU %d", currentSecond)
                lifecycleScope.launchWhenStarted { observeOnInternetAccess() }
                delay(1000)
            }
            binding.timerGroup.visibility = View.GONE
            setOnTryAgainLaterClicked()
        }
    }


    private suspend fun observeOnInternetAccess() {
        internetAccessObserver.getObserveOnIntentAccess().collect {
            when (it) {
                AVAILABLE -> this.dismiss()
                UNAVAILABLE -> {
                    Timber.e("UnAvailable")
                    setLoadingAnimationVisibility()
                }
            }
        }
    }

    private fun setLoadingAnimationVisibility() {
        binding.timerGroup.visibility = View.VISIBLE
    }

    private fun setOnTryAgainLaterClicked() {
        binding.apply {
            restartAppBtn.visibility = View.VISIBLE
            loadingAnim.visibility = View.GONE
            restartAppBtn.setOnClickListener {
                handleApplicationRestart()
            }
        }
    }

    private fun handleApplicationRestart() {
        ProcessPhoenix.triggerRebirth(context)
    }
}