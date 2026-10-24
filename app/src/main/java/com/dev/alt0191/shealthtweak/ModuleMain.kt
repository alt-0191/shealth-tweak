package com.dev.alt0191.shealthtweak

import android.util.Log
import com.dev.alt0191.shealthtweak.hook.DevModeHook
import com.dev.alt0191.shealthtweak.hook.RegionHook
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam

class ModuleMain : XposedModule() {

    override fun onModuleLoaded(param: ModuleLoadedParam) {
        log(Log.INFO, TAG, "loaded in ${param.processName}, framework=$frameworkName($frameworkVersionCode) API $apiVersion")
    }

    override fun onPackageLoaded(param: PackageLoadedParam) {
        if (param.packageName == FeatureFlags.TARGET_PACKAGE) {
            log(Log.INFO, TAG, "onPackageLoaded: ${param.packageName}")
        }
    }

    override fun onPackageReady(param: PackageReadyParam) {
        if (param.packageName != FeatureFlags.TARGET_PACKAGE) return
        if (!param.isFirstPackage) return

        val cl = param.classLoader
        val prefs = runCatching { getRemotePreferences(FeatureFlags.GROUP) }.getOrNull()

        val devMode = FeatureFlags.readBoolean(prefs, FeatureFlags.KEY_DEV_MODE, FeatureFlags.DEFAULT_DEV_MODE)
        val region = FeatureFlags.readBoolean(prefs, FeatureFlags.KEY_REGION, FeatureFlags.DEFAULT_REGION)
        val country = FeatureFlags.countryOf(FeatureFlags.readCountry(prefs))

        if (devMode) {
            runCatching { DevModeHook.install(this, cl) }
                .onFailure { log(Log.ERROR, TAG, "DevModeHook failed: $it") }
        }
        if (region) {
            runCatching { RegionHook.install(this, cl, country) }
                .onFailure { log(Log.ERROR, TAG, "RegionHook failed: $it") }
        }

        log(Log.INFO, TAG, "hooks installed (devMode=$devMode region=$region country=${country.code})")
    }

    private companion object {
        const val TAG = "SHealthTweak"
    }
}
