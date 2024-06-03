package com.example.testapplication.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class CheckConnectivity(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {
    companion object {
        @Volatile
        private var instance: CheckConnectivity? = null

        fun getInstance(connectivityManager: ConnectivityManager): CheckConnectivity {
            return instance ?: synchronized(this) {
                instance ?: CheckConnectivity(connectivityManager).also { instance = it }
            }
        }
    }
    
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            //logD("Internet", "onAvailable")
            //postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            //logD("Internet", "onLost")
            postValue(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities) {
            val isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            postValue(isInternet && isValidated)

            //logD("Internet", "networkCapabilities: $network $networkCapabilities")

            if (isValidated){
                //logD("Internet", "hasCapability: $network $networkCapabilities")
            } else {
                //logD("Internet", "Network has No Connection Capability: $network $networkCapabilities")
            }
        }

        override fun onUnavailable() {
            super.onUnavailable()
            //logD("Internet", "onUnavailable")
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        try {
            val builder = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            // Because when Internet (All of internet sources - Data, Wifi or TRANSPORT_CELLULAR) is off then none of networkCallback are called initially
            postValue(isNetworkAvailable())
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
            //logD("InternetLiveData", "Active")
        } catch (e: Exception){
            //logD("InternetLiveData", "Exception - ${e.message}")
        }
    }

    override fun onInactive() {
        super.onInactive()
        //logD("InternetLiveData", "in Active")
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun isNetworkAvailable(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    //logI("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    //logI("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    //logI("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    true
                }
                else -> false
            }
        } else false
    }
}