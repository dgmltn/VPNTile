# VPN Tile

A Quick Settings tile that shows whether a VPN is active, for any VPN app. Tap it to open
Android’s VPN settings; long-press it to open the app.

<!-- Screenshot: add docs/screenshot.png -->

## Install

Download the latest `VPNTile-<version>.apk` from
[Releases](https://github.com/dgmltn/VPNTile/releases) and open it on your phone.
Requires Android 13 or newer.

Then open the app and tap **Add tile to Quick Settings**.

## What it can’t do

- **Turn VPNs on or off.** Android only lets the VPN app itself do that; the tile opens VPN
  settings instead.
- **Name the VPN app.** Android doesn’t tell other apps who owns the VPN.
- **See a per-app VPN that excludes this app.** The tile reports the VPN that applies to its
  own traffic.

## Building

Needs JDK 21 (Gradle downloads one if it’s missing).

```bash
./gradlew assembleDebug        # debug APK
./gradlew testDebugUnitTest    # unit and Robolectric tests
```

## Releasing

1. Bump `versionName` and `versionCode` in `app/build.gradle.kts`.
2. Tag `v<versionName>` and push the tag. The release workflow builds a signed APK and attaches
   it to a GitHub Release.

One-time signing setup:

```bash
keytool -genkeypair -v -keystore release.jks -keyalg RSA -keysize 4096 -validity 10000 -alias vpntile
base64 -i release.jks | gh secret set SIGNING_KEYSTORE_BASE64
gh secret set SIGNING_STORE_PASSWORD
gh secret set SIGNING_KEY_ALIAS        # vpntile
gh secret set SIGNING_KEY_PASSWORD
```

Keep `release.jks` out of the repo (it’s in `.gitignore`) and back it up: losing it means
users can’t update in place.

## License

Apache 2.0. See [LICENSE](LICENSE).
