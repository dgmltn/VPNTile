# VPN Tile — Design Spec

Date: 2026-10-07
Source: Obsidian note `Apps/VPN Tile.md`, refined in brainstorming.

A Quick Settings tile that shows whether a VPN is currently active and opens the system VPN
settings screen when tapped. Android only (not KMP). Open source on GitHub, distributed as signed
APKs on GitHub Releases.

## Goals

- At-a-glance VPN status in the Quick Settings shade, alongside WiFi and flashlight
- One tap to jump to system VPN settings
- Works with any VPN app, not just one I control
- The app's own screens look like they belong in the system Settings app

## Non-goals

- Toggling a VPN. There is no public API to toggle another app's VPN; the tile reports status and
  deep-links to settings instead.
- A status bar icon. Apps can't add their own, and Android already shows a key icon when any VPN
  is up.
- Being a VPN. (See "Future: controlling my own VPN".)
- Localization beyond English (strings live in `strings.xml`, so it can be added later).

## Constraints

- Only a device owner / profile owner can force a VPN via
  `DevicePolicyManager.setAlwaysOnVpnPackage`.
- `NetworkCapabilities` does not reveal which app owns the VPN to a non-owner app, so the tile
  shows on/off only, not the VPN app's name.

## Identity

| | |
|---|---|
| App name | VPN Tile |
| applicationId / package | `com.dgmltn.vpntile` |
| Author | Doug Melton |
| Repo | https://github.com/dgmltn/VPNTile |
| License | Apache 2.0 |
| minSdk | 33 |
| compile/targetSdk | latest stable at implementation time |

## Architecture

Single `:app` module. No DI library and no ViewModels; the few objects are wired by hand.

```
com.dgmltn.vpntile
├── MainActivity            edge-to-edge, VpnTileTheme, Navigation 3 (Main ↔ About)
├── vpn/VpnStatusMonitor    source of truth for "is a VPN up"
├── tile/VpnTileService     the Quick Settings tile
├── tile/TileAdder          requestAddTileService wrapper
├── ui/main/                MainScreen (stateful) + MainContent (stateless) + widgets
├── ui/about/               AboutScreen
└── designsystem/           VpnTileTheme, VpnTilePreview, VpnTileIcon
```

### `VpnStatusMonitor`

- Constructed with a `Context`; uses `ConnectivityManager`.
- `val isVpnActive: Flow<Boolean>` — a `callbackFlow` that registers a default network callback
  on collection start (`onCapabilitiesChanged`, `onLost`), emits the current value first,
  unregisters on close, and is `distinctUntilChanged`.
- `fun isVpnActive(): Boolean` — one-shot read of
  `getNetworkCapabilities(activeNetwork)?.hasTransport(TRANSPORT_VPN) == true`.
- A missing `ConnectivityManager` is treated as "no VPN".

### `VpnTileService`

- `onStartListening`: set tile state from the one-shot read immediately, then collect
  `isVpnActive` in a scope that `onStopListening` cancels. Callbacks only matter while the shade
  is open, which is the only time the tile is visible; the initial read catches changes made
  while it was closed.
- Tile rendering: label "VPN"; active → `STATE_ACTIVE`, subtitle "Connected"; inactive →
  `STATE_INACTIVE`, subtitle "Off".
- `onClick`: open `Settings.ACTION_VPN_SETTINGS` via `startActivityAndCollapse` — the
  `PendingIntent` overload on API 34+, the deprecated `Intent` overload on API 33.
- Manifest: `BIND_QUICK_SETTINGS_TILE` permission, `QS_TILE` intent filter, monochrome key icon,
  `android.service.quicksettings.TOGGLEABLE_TILE` meta-data so accessibility announces it as a
  switch like the system tiles.
- Long-press: `MainActivity` handles `TileService.ACTION_QS_TILE_PREFERENCES`, so long-pressing
  the tile opens the app (system convention). Tap = VPN settings; long-press = this app.

### `TileAdder`

- Wraps `StatusBarManager.requestAddTileService(...)` and maps the result code to a sealed result:
  `Added`, `AlreadyAdded`, `NotAdded` (user declined), `Error(code)`.

### Manifest permissions

- `ACCESS_NETWORK_STATE` only.

## UI

Matches current Android Settings (Material 3 Expressive): dynamic color, `LargeTopAppBar` that
collapses on scroll, and sections of Material 3 `ListItem`s in rounded groups (large outer
corners, small corners between items in a group). Static fallback color schemes are used only
where dynamic color is unavailable (previews).

### Main screen

Title "VPN Tile".

1. **Status header** — large rounded container.
   - VPN up: filled key icon on `primaryContainer`, "VPN connected".
   - No VPN: outlined key icon on `surfaceContainerHigh`, "No VPN active".
   - Colors animate between states.
2. **Section "Quick Settings"**
   - "Add tile to Quick Settings" / "Shows VPN status in your notification shade" → runs
     `TileAdder`; result shown in a snackbar ("Tile added", "Tile is already added",
     "Tile not added", "Couldn't add tile").
3. **Section "VPN"**
   - "VPN settings" / "Manage VPN connections" → opens `ACTION_VPN_SETTINGS`.
4. **Section (single item)**
   - "About" → navigates to About.
5. **Footer** (`bodySmall`): "This app can't turn VPNs on or off. Android only lets VPN apps do
   that."

`MainScreen` (stateful) collects `VpnStatusMonitor.isVpnActive` with
`collectAsStateWithLifecycle` and owns the snackbar; `MainContent` (stateless) takes
`isVpnActive: Boolean` and callbacks.

### About screen

Modeled on Pulse's About screen (`HackerNews-KMP`,
`composeApp/src/commonMain/kotlin/presentation/screens/about/AboutScreen.kt`), Android-only:

- `TopAppBar` "About" with back arrow.
- App icon on a rounded-square background (plain `RoundedCornerShape`; no squircle library),
  "VPN Tile" (`headlineMedium`), "Version {versionName} ({versionCode}) • Doug Melton"
  (`titleMedium`, from `BuildConfig`), GitHub URL as a `TextButton`.
- "Open Source Libraries" — cards with project link (external-link icon + name), license link,
  and one-line description; sorted by name. Only libraries actually shipped: Kotlin, Kotlin
  Coroutines, Jetpack Compose, Material 3, Navigation 3, AndroidX Lifecycle, AndroidX Activity.

### Icons

- Launcher: adaptive icon with a monochrome layer (themed icons on Android 13+).
- Tile: monochrome key vector; the system tints it per state.
- In-app: every icon goes through the `VpnTileIcon` enum in `designsystem/`; screens never
  reference `Icons.*` directly. (Single module, so this is enforced by convention, not the build.)

### Previews

Every stateless composable has `Preview_*` functions wrapped in `VpnTilePreview`, covering its
meaningful states: `MainContent` (VPN on / off), status header (on / off), list section,
`AboutContent`, library card, and `Preview_VpnTileIconGallery`.

## Build

- AGP 9 (built-in Kotlin), Gradle version catalog, Compose BOM, Material 3, Navigation 3,
  `lifecycle-runtime-compose`. Versions pinned to latest stable at implementation time.
- `buildConfig = true` for version display.
- Release: R8 with `isMinifyEnabled` and `isShrinkResources`.
- Signing from env vars `SIGNING_KEYSTORE_BASE64` (decoded by CI to a file),
  `SIGNING_STORE_PASSWORD`, `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`. If absent, the release
  build is unsigned and still succeeds locally.
- `versionName` / `versionCode` bumped by hand in `app/build.gradle.kts` before tagging.
  Initial: `1.0.0` / `1`.

## CI / Release (GitHub Actions)

- `ci.yml` — on push to `main` and PRs: `./gradlew lint testDebugUnitTest assembleDebug`.
- `release.yml` — on `v*` tag: decode keystore from secrets, `assembleRelease`, create a GitHub
  Release with `VPNTile-<versionName>.apk` attached.

## Testing

JVM tests with Robolectric:

- `VpnStatusMonitorTest` — using `ShadowConnectivityManager` / `ShadowNetworkCapabilities`: emits
  `false` initially, `true` when the default network gains `TRANSPORT_VPN`, `false` after loss;
  callback unregistered when collection is cancelled.
- `TileAdderTest` — each `StatusBarManager` result code maps to the right `TileAdder` result.
- Compose UI tests for `MainContent` — status text in both states; each row invokes its callback.

Manual checklist (not automated):

- [ ] Add tile via in-app button; "already added" path on second tap
- [ ] Tile shows Connected/Off correctly when shade opens after VPN state changed while closed
- [ ] Tile updates live while shade is open
- [ ] Tap opens VPN settings and collapses shade (API 33 and 34+)
- [ ] Long-press opens the app
- [ ] Test with a few third-party VPN apps (e.g. WireGuard, Tailscale, a commercial VPN)
- [ ] Light/dark and several wallpapers (dynamic color)

## Repo extras

- `README.md` — what it does, screenshot placeholder, install from Releases, why it can't toggle
  VPNs.

## Future: controlling my own VPN

Out of scope; recorded from the original note in case the app grows:

- `VpnService.prepare()` for consent, `Builder().establish()` to connect, close the
  `ParcelFileDescriptor` + `stopSelf()` to disconnect.
- Service needs `BIND_VPN_SERVICE` permission and `foregroundServiceType="systemExempted"` on
  Android 14+.
- Platform IKEv2 alternative (API 30+): `VpnManager.provisionVpnProfile`,
  `startProvisionedVpnProfileSession`, `stopProvisionedVpnProfile`.
