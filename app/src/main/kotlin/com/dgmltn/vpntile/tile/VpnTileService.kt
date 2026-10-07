package com.dgmltn.vpntile.tile

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Build
import android.service.quicksettings.TileService
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.vpn.VpnStatusMonitor
import com.dgmltn.vpntile.vpn.vpnSettingsIntent
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class VpnTileService : TileService() {

    private val vpnStatusMonitor by lazy { VpnStatusMonitor(this) }
    private val scope = MainScope()
    private var listening: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        // The shade may have been closed while the VPN changed, so render before the first callback.
        render(vpnStatusMonitor.isVpnActiveNow())
        listening = scope.launch { vpnStatusMonitor.isVpnActive.collect(::render) }
    }

    override fun onStopListening() {
        listening?.cancel()
        listening = null
        super.onStopListening()
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onClick() {
        super.onClick()
        if (isLocked) {
            unlockAndRun { openVpnSettings() }
        } else {
            openVpnSettings()
        }
    }

    // The Intent overload is only reached on API 33, where it is still supported.
    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun openVpnSettings() {
        val intent = vpnSettingsIntent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE))
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    private fun render(isVpnActive: Boolean) {
        val tile = qsTile ?: return
        val appearance = tileAppearance(isVpnActive)
        tile.state = appearance.state
        tile.label = getString(R.string.tile_label)
        tile.subtitle = getString(appearance.subtitle)
        tile.updateTile()
    }
}
