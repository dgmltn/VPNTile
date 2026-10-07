package com.dgmltn.vpntile.ui.main

import androidx.annotation.StringRes
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.tile.TileAddResult

@StringRes
fun TileAddResult.messageRes(): Int? = when (this) {
    TileAddResult.Added -> R.string.tile_add_added
    TileAddResult.AlreadyAdded -> R.string.tile_add_already_added
    TileAddResult.NotAdded -> R.string.tile_add_not_added
    TileAddResult.InProgress -> null
    is TileAddResult.Error -> R.string.tile_add_error
}
