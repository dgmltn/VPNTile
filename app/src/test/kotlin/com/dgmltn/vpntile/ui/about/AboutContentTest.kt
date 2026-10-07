package com.dgmltn.vpntile.ui.about

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.designsystem.VpnTileTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AboutContentTest {

    @get:Rule
    val compose = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val openedUrls = mutableListOf<String>()
    private var backClicks = 0

    private fun show() {
        compose.setContent {
            VpnTileTheme {
                AboutContent(
                    versionName = "1.2.3",
                    versionCode = 45,
                    onBack = { backClicks++ },
                    onOpenUrl = { openedUrls += it },
                )
            }
        }
    }

    @Test
    fun `shows version and author`() {
        show()

        compose.onNodeWithText("Version 1.2.3 (45) • Doug Melton").assertIsDisplayed()
    }

    @Test
    fun `GitHub link opens the repository`() {
        show()

        compose.onNodeWithText(GITHUB_URL).performClick()

        assertEquals(listOf("https://github.com/dgmltn/VPNTile"), openedUrls)
    }

    @Test
    fun `back arrow goes back`() {
        show()

        compose.onNodeWithContentDescription(context.getString(R.string.back)).performClick()

        assertEquals(1, backClicks)
    }

    @Test
    fun `library name opens its project page`() {
        show()

        compose.onNodeWithText("Kotlin Coroutines").performScrollTo().performClick()

        assertEquals(listOf("https://github.com/Kotlin/kotlinx.coroutines"), openedUrls)
    }
}
