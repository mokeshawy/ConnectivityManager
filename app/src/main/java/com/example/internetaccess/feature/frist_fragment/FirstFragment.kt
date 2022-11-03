package com.example.internetaccess.feature.frist_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.genericsnackbar.core.utils.SnackBarBuilder
import com.example.internetaccess.R
import com.example.internetaccess.core.base_fragment.BaseFragment
import com.example.internetaccess.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding, FirstFragmentViewModel>() {

    override val binding by lazy { FragmentFirstBinding.inflate(layoutInflater) }
    override val viewModel: FirstFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testBtn.setOnClickListener {
            SnackBarBuilder().setMessage(R.string.msg_please_wait).setStartIcon(R.drawable.ic_baseline_arrow_forward_ios_24).setStartActionBtn(){

            }.build(requireActivity(),binding.root).show()
        }
    }

    private fun getHttpRequest(){
        val text = StringBuffer()
        var httpURLConnection: HttpURLConnection
        var inputStreamReader: InputStreamReader
        var buff: BufferedReader

        try {
            Thread {
                val url = URL("https://instabug.com")
                httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connect()
                inputStreamReader = InputStreamReader(httpURLConnection.content as InputStream)
                buff = BufferedReader(inputStreamReader)


                // write your code here
                var line: String? = "anything"
                while (line != null) {
                    line = buff.readLine()
                    Timber.e("${text.append(line)}")
                    Handler(Looper.getMainLooper()).post {
                        //binding.webView.loadData(line, "text/html", "UTF-8")
                    }
                }
            }.start()
        } catch (e: Exception) {
            Timber.e("error $e")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }
}