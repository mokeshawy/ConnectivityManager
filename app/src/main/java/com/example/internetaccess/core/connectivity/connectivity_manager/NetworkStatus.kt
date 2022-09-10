package com.example.internetaccess.core.connectivity.connectivity_manager

import android.net.Network

interface NetworkStatus {
    fun onAvailable(network: Network)
    fun onLost(network: Network)
}