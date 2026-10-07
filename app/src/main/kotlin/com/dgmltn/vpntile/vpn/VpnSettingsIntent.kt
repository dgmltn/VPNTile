package com.dgmltn.vpntile.vpn

import android.content.Context
import android.content.Intent
import android.provider.Settings

/** System VPN settings, or the main Settings screen on devices that don't have a VPN page. */
fun Context.vpnSettingsIntent(): Intent {
    val vpnSettings = Intent(Settings.ACTION_VPN_SETTINGS)
    val target = if (vpnSettings.resolveActivity(packageManager) != null) {
        vpnSettings
    } else {
        Intent(Settings.ACTION_SETTINGS)
    }
    return target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
