package com.dgmltn.vpntile.ui.main

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.designsystem.VpnTileTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainContentTest {

    @get:Rule
    val compose = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun show(
        isVpnActive: Boolean = false,
        onAddTile: () -> Unit = {},
        onOpenVpnSettings: () -> Unit = {},
        onOpenAbout: () -> Unit = {},
    ) {
        compose.setContent {
            VpnTileTheme {
                MainContent(
                    isVpnActive = isVpnActive,
                    snackbarHostState = remember { SnackbarHostState() },
                    onAddTile = onAddTile,
                    onOpenVpnSettings = onOpenVpnSettings,
                    onOpenAbout = onOpenAbout,
                )
            }
        }
    }

    @Test
    fun `shows connected when a VPN is active`() {
        show(isVpnActive = true)

        compose.onNodeWithText(context.getString(R.string.status_vpn_connected)).assertIsDisplayed()
    }

    @Test
    fun `shows no VPN when none is active`() {
        show(isVpnActive = false)

        compose.onNodeWithText(context.getString(R.string.status_no_vpn)).assertIsDisplayed()
    }

    @Test
    fun `add tile row requests the tile`() {
        var calls = 0
        show(onAddTile = { calls++ })

        compose.onNodeWithText(context.getString(R.string.add_tile_title)).performScrollTo().performClick()

        assertEquals(1, calls)
    }

    @Test
    fun `VPN settings row opens VPN settings`() {
        var calls = 0
        show(onOpenVpnSettings = { calls++ })

        compose.onNodeWithText(context.getString(R.string.vpn_settings_title)).performScrollTo().performClick()

        assertEquals(1, calls)
    }

    @Test
    fun `About row opens About`() {
        var calls = 0
        show(onOpenAbout = { calls++ })

        compose.onNodeWithText(context.getString(R.string.about_title)).performScrollTo().performClick()

        assertEquals(1, calls)
    }
}
