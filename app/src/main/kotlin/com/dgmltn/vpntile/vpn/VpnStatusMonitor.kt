package com.dgmltn.vpntile.vpn

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class VpnStatusMonitor(context: Context) {

    private val connectivityManager: ConnectivityManager? =
        context.getSystemService(ConnectivityManager::class.java)

    fun isVpnActiveNow(): Boolean {
        val connectivityManager = connectivityManager ?: return false
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork).hasVpnTransport()
    }

    /** The current VPN state, then every change to it. Listens to the system only while collected. */
    val isVpnActive: Flow<Boolean> = callbackFlow {
        val connectivityManager = connectivityManager
        if (connectivityManager == null) {
            send(false)
            awaitClose()
            return@callbackFlow
        }
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(capabilities.hasVpnTransport())
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        send(isVpnActiveNow())
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}

private fun NetworkCapabilities?.hasVpnTransport(): Boolean =
    this?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
