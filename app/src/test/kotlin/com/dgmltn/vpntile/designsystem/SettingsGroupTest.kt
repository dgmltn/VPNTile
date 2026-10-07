package com.dgmltn.vpntile.designsystem

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsGroupTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `title is shown as a heading`() {
        compose.setContent {
            VpnTileTheme {
                SettingsGroup(title = "Network") {
                    SettingsItem(title = "First", icon = VpnTileIcon.Settings, onClick = {})
                }
            }
        }

        compose.onNodeWithText("Network")
            .assertIsDisplayed()
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
    }

    @Test
    fun `each item invokes its own callback`() {
        var firstClicks = 0
        var secondClicks = 0
        compose.setContent {
            VpnTileTheme {
                SettingsGroup {
                    SettingsItem(title = "First", icon = VpnTileIcon.Settings, onClick = { firstClicks++ })
                    SettingsItem(
                        title = "Second",
                        summary = "Details",
                        icon = VpnTileIcon.About,
                        onClick = { secondClicks++ },
                    )
                }
            }
        }

        compose.onNodeWithText("Second").performClick()

        assertEquals(0, firstClicks)
        assertEquals(1, secondClicks)
        compose.onNodeWithText("Details").assertIsDisplayed()
    }
}
