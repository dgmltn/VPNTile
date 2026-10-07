package com.dgmltn.vpntile.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VpnTilePreview(
    padding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    VpnTileTheme {
        Surface {
            Box(modifier = Modifier.padding(padding), content = content)
        }
    }
}
