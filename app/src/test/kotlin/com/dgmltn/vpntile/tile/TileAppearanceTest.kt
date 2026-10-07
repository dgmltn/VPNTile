package com.dgmltn.vpntile.tile

import android.service.quicksettings.Tile
import com.dgmltn.vpntile.R
import org.junit.Assert.assertEquals
import org.junit.Test

class TileAppearanceTest {

    @Test
    fun `active VPN shows an active tile labelled Connected`() {
        assertEquals(
            TileAppearance(state = Tile.STATE_ACTIVE, subtitle = R.string.tile_subtitle_connected),
            tileAppearance(isVpnActive = true),
        )
    }

    @Test
    fun `no VPN shows an inactive tile labelled Off`() {
        assertEquals(
            TileAppearance(state = Tile.STATE_INACTIVE, subtitle = R.string.tile_subtitle_off),
            tileAppearance(isVpnActive = false),
        )
    }
}
