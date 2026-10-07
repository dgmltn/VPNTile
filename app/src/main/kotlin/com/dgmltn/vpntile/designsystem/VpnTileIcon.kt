package com.dgmltn.vpntile.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnKeyOff
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class VpnTileIcon(private val vector: ImageVector) {
    About(Icons.Outlined.Info),
    AddTile(Icons.Outlined.AddCircleOutline),
    Back(Icons.AutoMirrored.Filled.ArrowBack),
    OpenExternal(Icons.AutoMirrored.Filled.OpenInNew),
    Settings(Icons.Outlined.Settings),
    VpnOff(Icons.Filled.VpnKeyOff),
    VpnOn(Icons.Filled.VpnKey),
    ;

    val painter: Painter
        @Composable get() = rememberVectorPainter(vector)

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
        tint: Color = LocalContentColor.current,
    ) = VpnTileIcon(icon = this, contentDescription = contentDescription, modifier = modifier, tint = tint)
}

/** Renders a [VpnTileIcon] via Material 3 [Icon]. */
@Composable
fun VpnTileIcon(
    icon: VpnTileIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(painter = icon.painter, contentDescription = contentDescription, modifier = modifier, tint = tint)
}

@Preview
@Composable
private fun Preview_VpnTileIconGallery() {
    VpnTilePreview {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            VpnTileIcon.entries.forEach { icon ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    icon()
                    Text(icon.name)
                }
            }
        }
    }
}
