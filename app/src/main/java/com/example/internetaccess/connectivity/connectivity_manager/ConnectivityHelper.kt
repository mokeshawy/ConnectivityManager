package com.example.internetaccess.connectivity.connectivity_manager

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityHelper {

    val isNetworkConnected: StateFlow<Boolean>
}