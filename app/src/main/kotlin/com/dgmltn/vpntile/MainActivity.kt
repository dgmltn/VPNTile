package com.dgmltn.vpntile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dgmltn.vpntile.designsystem.VpnTileTheme
import com.dgmltn.vpntile.tile.TileAdder
import com.dgmltn.vpntile.vpn.VpnStatusMonitor

class MainActivity : ComponentActivity() {

    private val vpnStatusMonitor by lazy { VpnStatusMonitor(applicationContext) }
    private val tileAdder by lazy { TileAdder(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            VpnTileTheme {
                VpnTileApp(vpnStatusMonitor = vpnStatusMonitor, tileAdder = tileAdder)
            }
        }
    }
}
