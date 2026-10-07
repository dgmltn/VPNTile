package com.dgmltn.vpntile.tile

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Remembers whether the tile is in Quick Settings. Android doesn't let apps ask, so
 * [VpnTileService] records it as the system adds, removes and binds the tile.
 */
class TileAddedStore(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isAddedNow(): Boolean = preferences.getBoolean(KEY_ADDED, false)

    fun setAdded(added: Boolean) {
        preferences.edit { putBoolean(KEY_ADDED, added) }
    }

    val isAdded: Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_ADDED) trySend(isAddedNow())
        }
        send(isAddedNow())
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.distinctUntilChanged()
}

fun TileAddResult.meansTileIsAdded(): Boolean =
    this == TileAddResult.Added || this == TileAddResult.AlreadyAdded

private const val PREFERENCES_NAME = "tile"
private const val KEY_ADDED = "added"
