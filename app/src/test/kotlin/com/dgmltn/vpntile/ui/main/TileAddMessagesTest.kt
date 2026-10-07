package com.dgmltn.vpntile.ui.main

import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.tile.TileAddResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TileAddMessagesTest {

    @Test
    fun `each result has its own message`() {
        assertEquals(R.string.tile_add_added, TileAddResult.Added.messageRes())
        assertEquals(R.string.tile_add_already_added, TileAddResult.AlreadyAdded.messageRes())
        assertEquals(R.string.tile_add_not_added, TileAddResult.NotAdded.messageRes())
    }

    @Test
    fun `any error code shows the generic error`() {
        assertEquals(R.string.tile_add_error, TileAddResult.Error(1001).messageRes())
        assertEquals(R.string.tile_add_error, TileAddResult.Error(4242).messageRes())
    }

    @Test
    fun `a request already in progress shows no message`() {
        assertNull(TileAddResult.InProgress.messageRes())
    }
}
