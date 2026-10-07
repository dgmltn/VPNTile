package com.dgmltn.vpntile.tile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VpnTileServiceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val store = TileAddedStore(context)
    // Not destroyed afterwards: Robolectric's TileService shadow crashes in onDestroy.
    private val service = Robolectric.buildService(VpnTileService::class.java).create().get()

    @Test
    fun `adding the tile records it`() {
        service.onTileAdded()

        assertTrue(store.isAddedNow())
    }

    @Test
    fun `removing the tile records it`() {
        store.setAdded(true)

        service.onTileRemoved()

        assertFalse(store.isAddedNow())
    }

    @Test
    fun `listening proves the tile is added`() {
        service.onStartListening()
        service.onStopListening()

        assertTrue(store.isAddedNow())
    }
}
