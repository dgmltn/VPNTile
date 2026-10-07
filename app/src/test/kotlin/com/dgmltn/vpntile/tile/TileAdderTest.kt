package com.dgmltn.vpntile.tile

import android.app.StatusBarManager
import org.junit.Assert.assertEquals
import org.junit.Test

class TileAdderTest {

    @Test
    fun `tile added maps to Added`() {
        assertEquals(TileAddResult.Added, tileAddResultOf(StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED))
    }

    @Test
    fun `tile already added maps to AlreadyAdded`() {
        assertEquals(
            TileAddResult.AlreadyAdded,
            tileAddResultOf(StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED),
        )
    }

    @Test
    fun `user declining maps to NotAdded`() {
        assertEquals(TileAddResult.NotAdded, tileAddResultOf(StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED))
    }

    @Test
    fun `a second request while the prompt is showing maps to InProgress`() {
        assertEquals(
            TileAddResult.InProgress,
            tileAddResultOf(StatusBarManager.TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS),
        )
    }

    @Test
    fun `system errors keep their code`() {
        val code = StatusBarManager.TILE_ADD_REQUEST_ERROR_BAD_COMPONENT
        assertEquals(TileAddResult.Error(code), tileAddResultOf(code))
    }

    @Test
    fun `unknown codes are errors`() {
        assertEquals(TileAddResult.Error(4242), tileAddResultOf(4242))
    }
}
