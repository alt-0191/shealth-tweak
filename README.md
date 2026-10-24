# SHealth Tweak

[中文](README.zh-CN.md)

Xposed module for Samsung Health: unlock FeatureList debug features, bypass developer key verification, and spoof region.

## Features

- **Developer mode** — Skips `FeatureSettingOn` / `CscManagerOn` file detection and Developer Key validation, enabling all FeatureList debug toggles directly and revealing the developer entry in the About page.
- **Region spoof** — Rewrites the region (including Samsung Account country code) to the selected area to unlock region-locked features.

## Usage

1. Install the APK and enable `com.sec.android.app.shealth` in LSPosed (scope declared in `META-INF/xposed/scope.list`).
2. Open SHealth Tweak → toggle features (defaults: developer mode on, region spoof off).
3. Settings → About Samsung Health → SetFeatures to open the debug list.
4. Tap **Apply & restart** to force-stop and relaunch Samsung Health.

## Build

```bash
./gradlew assembleDebug
```

## Compatibility

- Samsung Health: 7.x global (verified on 7.1.0.053) — CN variant not supported
- Android: 10+ (minSdk 29)
- Framework: LSPosed (Modern Xposed API 102)

## License

MIT
