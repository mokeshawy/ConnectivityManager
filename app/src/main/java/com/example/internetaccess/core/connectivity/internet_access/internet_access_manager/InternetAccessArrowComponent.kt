package com.example.internetaccess.core.connectivity.internet_access.internet_access_manager

interface InternetAccessArrowComponent {
    fun onInternetAccessAvailable(internetAccess: Boolean)
    fun onInternetAccessUnAvailable(internetAccess: Boolean)
}