package com.dgmltn.vpntile

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.dgmltn.vpntile.tile.TileAddedStore
import com.dgmltn.vpntile.tile.TileAdder
import com.dgmltn.vpntile.ui.about.AboutScreen
import com.dgmltn.vpntile.ui.main.MainScreen
import com.dgmltn.vpntile.vpn.VpnStatusMonitor
import kotlinx.serialization.Serializable

@Serializable
data object MainKey : NavKey

@Serializable
data object AboutKey : NavKey

@Composable
fun VpnTileApp(
    vpnStatusMonitor: VpnStatusMonitor,
    tileAdder: TileAdder,
    tileAddedStore: TileAddedStore,
) {
    val backStack = rememberNavBackStack(MainKey)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainKey> {
                MainScreen(
                    vpnStatusMonitor = vpnStatusMonitor,
                    tileAdder = tileAdder,
                    tileAddedStore = tileAddedStore,
                    onOpenAbout = { backStack.add(AboutKey) },
                )
            }
            entry<AboutKey> {
                AboutScreen(onBack = { backStack.removeLastOrNull() })
            }
        },
    )
}
