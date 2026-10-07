package com.dgmltn.vpntile.tile

import android.service.quicksettings.Tile
import androidx.annotation.StringRes
import com.dgmltn.vpntile.R

data class TileAppearance(val state: Int, @StringRes val subtitle: Int)

fun tileAppearance(isVpnActive: Boolean): TileAppearance =
    if (isVpnActive) {
        TileAppearance(state = Tile.STATE_ACTIVE, subtitle = R.string.tile_subtitle_connected)
    } else {
        TileAppearance(state = Tile.STATE_INACTIVE, subtitle = R.string.tile_subtitle_off)
    }
