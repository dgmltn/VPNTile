package com.dgmltn.vpntile.tile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TileAddedStoreTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val store = TileAddedStore(context)

    @Test
    fun `tile is not added by default`() {
        assertFalse(store.isAddedNow())
    }

    @Test
    fun `a saved state survives a new store instance`() {
        store.setAdded(true)

        assertTrue(TileAddedStore(context).isAddedNow())
    }

    @Test
    fun `flow follows changes`() = runTest {
        store.isAdded.test {
            assertFalse(awaitItem())

            store.setAdded(true)
            assertTrue(awaitItem())

            store.setAdded(true)
            store.setAdded(false)
            assertFalse(awaitItem())
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `added and already added results mean the tile is in Quick Settings`() {
        assertTrue(TileAddResult.Added.meansTileIsAdded())
        assertTrue(TileAddResult.AlreadyAdded.meansTileIsAdded())
        assertFalse(TileAddResult.NotAdded.meansTileIsAdded())
        assertFalse(TileAddResult.InProgress.meansTileIsAdded())
        assertFalse(TileAddResult.Error(1001).meansTileIsAdded())
    }
}
