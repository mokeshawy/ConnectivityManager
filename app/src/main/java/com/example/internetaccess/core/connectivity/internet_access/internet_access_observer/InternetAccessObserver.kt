package com.example.internetaccess.core.connectivity.internet_access.internet_access_observer

import com.example.internetaccess.core.connectivity.internet_access.internet_access_state.InternetAccessState
import kotlinx.coroutines.flow.Flow

interface InternetAccessObserver {
    fun observeOnInternetAccess() : Flow<InternetAccessState>
}