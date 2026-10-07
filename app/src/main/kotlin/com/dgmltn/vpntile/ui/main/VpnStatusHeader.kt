package com.dgmltn.vpntile.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.designsystem.VpnTileIcon
import com.dgmltn.vpntile.designsystem.VpnTilePreview

@Composable
fun VpnStatusHeader(isVpnActive: Boolean, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (isVpnActive) colors.primaryContainer else colors.surfaceContainerHigh,
        label = "statusContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isVpnActive) colors.onPrimaryContainer else colors.onSurfaceVariant,
        label = "statusContent",
    )
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(contentColor.copy(alpha = 0.12f), CircleShape),
            ) {
                val icon = if (isVpnActive) VpnTileIcon.VpnOn else VpnTileIcon.VpnOff
                icon()
            }
            Text(
                text = stringResource(if (isVpnActive) R.string.status_vpn_connected else R.string.status_no_vpn),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Preview
@Composable
private fun Preview_VpnStatusHeader_Connected() {
    VpnTilePreview {
        VpnStatusHeader(isVpnActive = true)
    }
}

@Preview
@Composable
private fun Preview_VpnStatusHeader_NoVpn() {
    VpnTilePreview {
        VpnStatusHeader(isVpnActive = false)
    }
}
