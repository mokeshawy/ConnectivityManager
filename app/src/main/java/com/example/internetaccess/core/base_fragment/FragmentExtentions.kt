package com.example.internetaccess.core.base_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(action: Int, bundle: Bundle? = null) {
    findNavController().navigate(action, bundle)
}