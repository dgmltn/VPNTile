package com.dgmltn.vpntile.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val GroupShape = RoundedCornerShape(24.dp)
private val ItemShape = RoundedCornerShape(4.dp)

/**
 * A group of [SettingsItem]s styled like Android Settings. Clipping the group to large corners
 * while each item keeps small ones gives large outer corners and small corners between items.
 */
@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .semantics { heading() },
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.clip(GroupShape),
            content = content,
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    icon: VpnTileIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    iconContentDescription: String? = null,
) {
    Surface(
        onClick = onClick,
        shape = ItemShape,
        color = MaterialTheme.colorScheme.surfaceBright,
        modifier = modifier.fillMaxWidth(),
    ) {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = summary?.let { { Text(it) } },
            leadingContent = { icon(contentDescription = iconContentDescription) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        )
    }
}

@Preview
@Composable
private fun Preview_SettingsGroup() {
    VpnTilePreview {
        SettingsGroup(title = "Quick Settings") {
            SettingsItem(
                title = "Add tile to Quick Settings",
                summary = "Shows VPN status in your notification shade",
                icon = VpnTileIcon.AddTile,
                onClick = {},
            )
            SettingsItem(title = "VPN settings", icon = VpnTileIcon.Settings, onClick = {})
        }
    }
}

@Preview
@Composable
private fun Preview_SettingsGroup_SingleUntitled() {
    VpnTilePreview {
        SettingsGroup {
            SettingsItem(title = "About", icon = VpnTileIcon.About, onClick = {})
        }
    }
}

@Preview
@Composable
private fun Preview_SettingsItem_WithSummary() {
    VpnTilePreview {
        SettingsItem(
            title = "VPN settings",
            summary = "Manage VPN connections",
            icon = VpnTileIcon.Settings,
            onClick = {},
        )
    }
}
