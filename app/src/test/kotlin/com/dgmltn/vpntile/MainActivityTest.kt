package com.dgmltn.vpntile

import android.content.Context
import android.content.Intent
import android.service.quicksettings.TileService
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `About opens from the main screen and back returns`() {
        compose.onNodeWithText(context.getString(R.string.about_title)).performScrollTo().performClick()
        compose.onNodeWithText(context.getString(R.string.open_source_libraries)).assertIsDisplayed()

        compose.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }

        compose.onNodeWithText(context.getString(R.string.add_tile_title)).assertIsDisplayed()
    }

    @Test
    fun `long-pressing the tile opens this app`() {
        val intent = Intent(TileService.ACTION_QS_TILE_PREFERENCES).setPackage(context.packageName)

        val activities = context.packageManager.queryIntentActivities(intent, 0)

        assertEquals(MainActivity::class.java.name, activities.single().activityInfo.name)
    }
}
