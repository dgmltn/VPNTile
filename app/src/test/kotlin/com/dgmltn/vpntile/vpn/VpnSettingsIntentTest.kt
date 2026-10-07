package com.dgmltn.vpntile.vpn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class VpnSettingsIntentTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `opens VPN settings when the device has that page`() {
        val component = ComponentName("com.android.settings", "com.android.settings.VpnSettings")
        val packageManager = shadowOf(context.packageManager)
        packageManager.addActivityIfNotPresent(component)
        packageManager.addIntentFilterForActivity(
            component,
            IntentFilter(Settings.ACTION_VPN_SETTINGS).apply { addCategory(Intent.CATEGORY_DEFAULT) },
        )

        assertEquals(Settings.ACTION_VPN_SETTINGS, context.vpnSettingsIntent().action)
    }

    @Test
    fun `falls back to main settings when the device has no VPN settings page`() {
        assertEquals(Settings.ACTION_SETTINGS, context.vpnSettingsIntent().action)
    }

    @Test
    fun `starts in a new task`() {
        assertTrue(context.vpnSettingsIntent().flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
    }
}
