package com.dgmltn.vpntile.tile

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import com.dgmltn.vpntile.R
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

sealed interface TileAddResult {
    data object Added : TileAddResult
    data object AlreadyAdded : TileAddResult
    data object NotAdded : TileAddResult

    /** The system prompt from an earlier request is still showing. */
    data object InProgress : TileAddResult
    data class Error(val code: Int) : TileAddResult
}

internal fun tileAddResultOf(code: Int): TileAddResult = when (code) {
    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> TileAddResult.Added
    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> TileAddResult.AlreadyAdded
    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED -> TileAddResult.NotAdded
    StatusBarManager.TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS -> TileAddResult.InProgress
    else -> TileAddResult.Error(code)
}

private const val NO_STATUS_BAR_MANAGER = -1

class TileAdder(private val context: Context) {

    /** Shows the system prompt to add [VpnTileService] to Quick Settings. Call while in the foreground. */
    suspend fun requestAdd(): TileAddResult {
        val statusBarManager = context.getSystemService(StatusBarManager::class.java)
            ?: return TileAddResult.Error(NO_STATUS_BAR_MANAGER)
        return suspendCancellableCoroutine { continuation ->
            statusBarManager.requestAddTileService(
                ComponentName(context, VpnTileService::class.java),
                context.getString(R.string.tile_label),
                Icon.createWithResource(context, R.drawable.ic_vpn_key),
                context.mainExecutor,
            ) { code -> continuation.resume(tileAddResultOf(code)) }
        }
    }
}
