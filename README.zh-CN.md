# SHealth Tweak

[English](README.md)

三星健康 Xposed 模块：解锁 FeatureList 调试功能、绕过 Developer Key 验证、地区伪装。

## 功能

- **开发者模式** —— 跳过 `FeatureSettingOn` / `CscManagerOn` 文件检测与 Developer Key 验证，直接开启 FeatureList 全部调试功能，并在「关于」页露出开发者设置入口。
- **地区伪装** —— 把地区（含 Samsung Account 地区码）改写为所选地区，解除地区限制。

## 使用方法

1. 安装 APK，在 LSPosed 勾选 `com.sec.android.app.shealth`（作用域由 `META-INF/xposed/scope.list` 声明）
2. 打开 SHealth Tweak → 设置开关（默认：开发者模式开、地区伪装关）
3. Settings → About Samsung Health → SetFeatures 打开调试列表
4. 「应用并重启」强停并重启 Samsung Health

## 构建

```bash
./gradlew assembleDebug
```

## 兼容性

- Samsung Health：7.x global（7.1.0.053 已验证）/ cn 版本不可用
- Android：10+（minSdk 29）
- 框架：LSPosed（Modern Xposed API 102）

## 许可

MIT
