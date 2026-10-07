package com.dgmltn.vpntile.vpn

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowNetwork
import org.robolectric.shadows.ShadowNetworkCapabilities

@RunWith(RobolectricTestRunner::class)
class VpnStatusMonitorTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private val shadowConnectivityManager = shadowOf(connectivityManager)
    private val monitor = VpnStatusMonitor(context)
    private val network = ShadowNetwork.newInstance(100)

    @Test
    fun `isVpnActiveNow is false when the active network is not a VPN`() {
        shadowConnectivityManager.setNetworkCapabilities(connectivityManager.activeNetwork, capabilities(TRANSPORT_WIFI))

        assertFalse(monitor.isVpnActiveNow())
    }

    @Test
    fun `isVpnActiveNow is true when the active network is a VPN`() {
        shadowConnectivityManager.setNetworkCapabilities(
            connectivityManager.activeNetwork,
            capabilities(TRANSPORT_VPN, TRANSPORT_WIFI),
        )

        assertTrue(monitor.isVpnActiveNow())
    }

    @Test
    fun `flow starts with the current state then follows the default network`() = runTest {
        monitor.isVpnActive.test {
            assertFalse(awaitItem())
            val callback = shadowConnectivityManager.networkCallbacks.single()

            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_VPN, TRANSPORT_WIFI))
            assertTrue(awaitItem())

            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_WIFI))
            assertFalse(awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `losing the default network reports no VPN`() = runTest {
        monitor.isVpnActive.test {
            assertFalse(awaitItem())
            val callback = shadowConnectivityManager.networkCallbacks.single()
            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_VPN))
            assertTrue(awaitItem())

            callback.onLost(network)

            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repeated identical states are emitted once`() = runTest {
        monitor.isVpnActive.test {
            assertFalse(awaitItem())
            val callback = shadowConnectivityManager.networkCallbacks.single()

            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_VPN))
            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_VPN, TRANSPORT_WIFI))
            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_WIFI))
            callback.onCapabilitiesChanged(network, capabilities(TRANSPORT_WIFI))

            assertTrue(awaitItem())
            assertFalse(awaitItem())
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `stopping collection unregisters the network callback`() = runTest {
        monitor.isVpnActive.test {
            awaitItem()
            assertEquals(1, shadowConnectivityManager.networkCallbacks.size)
            cancelAndIgnoreRemainingEvents()
        }
        testScheduler.advanceUntilIdle()

        assertTrue(shadowConnectivityManager.networkCallbacks.isEmpty())
    }

    private fun capabilities(vararg transports: Int): NetworkCapabilities =
        ShadowNetworkCapabilities.newInstance().also { capabilities ->
            val shadow = Shadow.extract<ShadowNetworkCapabilities>(capabilities)
            transports.forEach { shadow.addTransportType(it) }
        }
}
