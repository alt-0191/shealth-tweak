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

## Samsung Health Assistant

The independent Assistant toggle enables the entry point, bypasses the feature disable switch and model/location checks, and selects the staging server (`ai_assistant_server_env=stg`). It also overrides any custom Assistant endpoint so the server selection takes effect. Account checks, consent and message limits still run normally.

Assistant was reported working on Samsung Health **7.1.9.019 (7019019)** on an SM-F971B using staging. The production setting returned “cannot connect to Samsung Health servers” during that test. Compatibility varies by Health version; the module does not block other versions from trying. In 7.1.3.001, the Assistant activity immediately finishes and its entry point is compiled out.

Assistant is enabled by default. Turning the toggle off hides the entry point and sets the Assistant disable flag. Use **Apply & restart** after changing it. This toggle works independently of Developer mode.
