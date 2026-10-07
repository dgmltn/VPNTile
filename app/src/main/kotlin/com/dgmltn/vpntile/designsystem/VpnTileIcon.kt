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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class VpnTileIcon(
    private val vector: ImageVector,
    private val hasOwnColors: Boolean = false,
) {
    About(Icons.Outlined.Info),
    AddTile(Icons.Outlined.AddCircleOutline),
    Back(Icons.AutoMirrored.Filled.ArrowBack),
    OpenExternal(Icons.AutoMirrored.Filled.OpenInNew),
    Settings(Icons.Outlined.Settings),
    TileAdded(SuccessCheck, hasOwnColors = true),
    VpnOff(Icons.Filled.VpnKeyOff),
    VpnOn(Icons.Filled.VpnKey),
    ;

    val painter: Painter
        @Composable get() = rememberVectorPainter(vector)

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
        tint: Color = if (hasOwnColors) Color.Unspecified else LocalContentColor.current,
    ) = VpnTileIcon(icon = this, contentDescription = contentDescription, modifier = modifier, tint = tint)
}

/** A white check on a green circle, the "done" mark used throughout Android Settings. */
private val SuccessCheck: ImageVector
    get() = ImageVector.Builder(
        name = "SuccessCheck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xFF1E8E3E))) {
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12f, 22f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12f, 2f)
            close()
        }
        group(scaleX = 0.6f, scaleY = 0.6f, pivotX = 12f, pivotY = 12f) {
            path(fill = SolidColor(Color.White)) {
                moveTo(9f, 16.17f)
                lineTo(4.83f, 12f)
                lineToRelative(-1.42f, 1.41f)
                lineTo(9f, 19f)
                lineTo(21f, 7f)
                lineToRelative(-1.41f, -1.41f)
                close()
            }
        }
    }.build()

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
