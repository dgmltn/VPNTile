package com.dgmltn.vpntile.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgmltn.vpntile.BuildConfig
import com.dgmltn.vpntile.R
import com.dgmltn.vpntile.designsystem.VpnTileIcon
import com.dgmltn.vpntile.designsystem.VpnTilePreview

private const val AUTHOR = "Doug Melton"
internal const val GITHUB_URL = "https://github.com/dgmltn/VPNTile"

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    AboutContent(
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
        onBack = onBack,
        onOpenUrl = uriHandler::openUri,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    versionName: String,
    versionCode: Int,
    onBack: () -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.surfaceContainer
    Scaffold(
        modifier = modifier,
        containerColor = background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        VpnTileIcon.Back(contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = background,
                    scrolledContainerColor = background,
                ),
            )
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            AppIconBadge()
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.about_version_author, versionName, versionCode, AUTHOR),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { onOpenUrl(GITHUB_URL) }) {
                Text(text = GITHUB_URL)
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.open_source_libraries),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .semantics { heading() },
            )
            Spacer(Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                openSourceLibraries.forEach { library ->
                    OpenSourceLibraryCard(
                        name = library.name,
                        description = library.description,
                        license = library.license,
                        onOpenProject = { onOpenUrl(library.projectUrl) },
                        onOpenLicense = { onOpenUrl(library.licenseUrl) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIconBadge(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(108.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .background(colorResource(R.color.ic_launcher_background), RoundedCornerShape(22.dp)),
        )
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_icon_content_description),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OpenSourceLibraryCard(
    name: String,
    description: String,
    license: String,
    onOpenProject: () -> Unit,
    onOpenLicense: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(16.dp)),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            itemVerticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextButton(onClick = onOpenProject) {
                VpnTileIcon.OpenExternal(modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = name, style = MaterialTheme.typography.titleMedium)
            }
            TextButton(onClick = onOpenLicense) {
                Text(text = license)
            }
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        )
    }
}

private data class OpenSourceLibrary(
    val name: String,
    val description: String,
    val license: String,
    val licenseUrl: String,
    val projectUrl: String,
)

private const val APACHE_2 = "Apache 2.0"
private const val ANDROIDX_LICENSE_URL = "https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt"

private val openSourceLibraries = listOf(
    OpenSourceLibrary(
        name = "AndroidX Activity",
        description = "Hosts the app’s Compose UI.",
        license = APACHE_2,
        licenseUrl = ANDROIDX_LICENSE_URL,
        projectUrl = "https://developer.android.com/jetpack/androidx/releases/activity",
    ),
    OpenSourceLibrary(
        name = "AndroidX Lifecycle",
        description = "Watches VPN status only while the screen is visible.",
        license = APACHE_2,
        licenseUrl = ANDROIDX_LICENSE_URL,
        projectUrl = "https://developer.android.com/jetpack/androidx/releases/lifecycle",
    ),
    OpenSourceLibrary(
        name = "Jetpack Compose",
        description = "UI toolkit for every screen.",
        license = APACHE_2,
        licenseUrl = ANDROIDX_LICENSE_URL,
        projectUrl = "https://developer.android.com/compose",
    ),
    OpenSourceLibrary(
        name = "Kotlin",
        description = "Language and standard library.",
        license = APACHE_2,
        licenseUrl = "https://github.com/JetBrains/kotlin/blob/master/license/LICENSE.txt",
        projectUrl = "https://kotlinlang.org",
    ),
    OpenSourceLibrary(
        name = "Kotlin Coroutines",
        description = "Follows VPN status changes as they happen.",
        license = APACHE_2,
        licenseUrl = "https://github.com/Kotlin/kotlinx.coroutines/blob/master/LICENSE.txt",
        projectUrl = "https://github.com/Kotlin/kotlinx.coroutines",
    ),
    OpenSourceLibrary(
        name = "kotlinx.serialization",
        description = "Saves the screen history across app restarts.",
        license = APACHE_2,
        licenseUrl = "https://github.com/Kotlin/kotlinx.serialization/blob/master/LICENSE.txt",
        projectUrl = "https://github.com/Kotlin/kotlinx.serialization",
    ),
    OpenSourceLibrary(
        name = "Material 3",
        description = "Material Design components and dynamic color.",
        license = APACHE_2,
        licenseUrl = ANDROIDX_LICENSE_URL,
        projectUrl = "https://developer.android.com/jetpack/androidx/releases/compose-material3",
    ),
    OpenSourceLibrary(
        name = "Navigation 3",
        description = "Moves between the main and About screens.",
        license = APACHE_2,
        licenseUrl = ANDROIDX_LICENSE_URL,
        projectUrl = "https://developer.android.com/guide/navigation/navigation-3",
    ),
).sortedBy { it.name.lowercase() }

@Preview
@Composable
private fun Preview_AboutContent() {
    VpnTilePreview(padding = 0.dp) {
        AboutContent(versionName = "1.0.0", versionCode = 1, onBack = {}, onOpenUrl = {})
    }
}

@Preview(name = "Narrow (250dp)", widthDp = 250)
@Preview(name = "Wide (600dp)", widthDp = 600)
@Composable
private fun Preview_OpenSourceLibraryCard() {
    VpnTilePreview {
        OpenSourceLibraryCard(
            name = "Kotlin Coroutines",
            description = "Follows VPN status changes as they happen.",
            license = "Apache 2.0",
            onOpenProject = {},
            onOpenLicense = {},
        )
    }
}
