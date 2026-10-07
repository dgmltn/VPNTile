package com.dgmltn.vpntile.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.designsystem.SettingsGroup
import com.dgmltn.vpntile.designsystem.SettingsItem
import com.dgmltn.vpntile.designsystem.VpnTileIcon
import com.dgmltn.vpntile.designsystem.VpnTilePreview
import com.dgmltn.vpntile.tile.TileAdder
import com.dgmltn.vpntile.vpn.VpnStatusMonitor
import com.dgmltn.vpntile.vpn.vpnSettingsIntent
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    vpnStatusMonitor: VpnStatusMonitor,
    tileAdder: TileAdder,
    onOpenAbout: () -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val isVpnActive by vpnStatusMonitor.isVpnActive.collectAsStateWithLifecycle(
        initialValue = remember(vpnStatusMonitor) { vpnStatusMonitor.isVpnActiveNow() },
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    MainContent(
        isVpnActive = isVpnActive,
        snackbarHostState = snackbarHostState,
        onAddTile = {
            scope.launch {
                val messageRes = tileAdder.requestAdd().messageRes() ?: return@launch
                snackbarHostState.showSnackbar(resources.getString(messageRes))
            }
        },
        onOpenVpnSettings = { context.startActivity(context.vpnSettingsIntent()) },
        onOpenAbout = onOpenAbout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    isVpnActive: Boolean,
    snackbarHostState: SnackbarHostState,
    onAddTile: () -> Unit,
    onOpenVpnSettings: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val background = MaterialTheme.colorScheme.surfaceContainer
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = background,
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = background,
                    scrolledContainerColor = background,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        ) {
            VpnStatusHeader(isVpnActive = isVpnActive)
            SettingsGroup(title = stringResource(R.string.section_quick_settings)) {
                SettingsItem(
                    title = stringResource(R.string.add_tile_title),
                    summary = stringResource(R.string.add_tile_summary),
                    icon = VpnTileIcon.AddTile,
                    onClick = onAddTile,
                )
            }
            SettingsGroup(title = stringResource(R.string.section_vpn)) {
                SettingsItem(
                    title = stringResource(R.string.vpn_settings_title),
                    summary = stringResource(R.string.vpn_settings_summary),
                    icon = VpnTileIcon.Settings,
                    onClick = onOpenVpnSettings,
                )
            }
            SettingsGroup {
                SettingsItem(
                    title = stringResource(R.string.about_title),
                    icon = VpnTileIcon.About,
                    onClick = onOpenAbout,
                )
            }
            Text(
                text = stringResource(R.string.main_footer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun Preview_MainContent_Connected() {
    VpnTilePreview(padding = 0.dp) {
        MainContent(
            isVpnActive = true,
            snackbarHostState = remember { SnackbarHostState() },
            onAddTile = {},
            onOpenVpnSettings = {},
            onOpenAbout = {},
        )
    }
}

@Preview
@Composable
private fun Preview_MainContent_NoVpn() {
    VpnTilePreview(padding = 0.dp) {
        MainContent(
            isVpnActive = false,
            snackbarHostState = remember { SnackbarHostState() },
            onAddTile = {},
            onOpenVpnSettings = {},
            onOpenAbout = {},
        )
    }
}
