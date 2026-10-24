package com.dev.alt0191.shealthtweak.prefs

import android.content.Context
import android.content.SharedPreferences
import com.dev.alt0191.shealthtweak.FeatureFlags
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper
object ModulePrefs {

    private const val LOCAL_FILE = "module_settings"

    @Volatile private var service: XposedService? = null
    @Volatile private var cached: SharedPreferences? = null
    @Volatile private var local: SharedPreferences? = null
    @Volatile private var started = false
    @Synchronized
    fun start(context: Context) {
        if (local == null) {
            local = context.applicationContext.getSharedPreferences(LOCAL_FILE, Context.MODE_PRIVATE)
        }
        if (started) return
        started = true
        runCatching {
            XposedServiceHelper.registerListener(object : XposedServiceHelper.OnServiceListener {
                override fun onServiceBind(service: XposedService) {
                    ModulePrefs.service = service
                    ModulePrefs.cached =
                        runCatching { service.getRemotePreferences(FeatureFlags.GROUP) }.getOrNull()
                    syncLocalToRemote()
                    com.dev.alt0191.shealthtweak.ModuleStatus.refresh(service)
                }

                override fun onServiceDied(service: XposedService) {
                    if (ModulePrefs.service === service) {
                        ModulePrefs.service = null
                        ModulePrefs.cached = null
                    }
                }
            })
        }
    }

    val isAvailable: Boolean get() = cached != null
    fun serviceOrNull(): XposedService? = service

    fun readDevMode(): Boolean =
        local?.getBoolean(FeatureFlags.KEY_DEV_MODE, FeatureFlags.DEFAULT_DEV_MODE)
            ?: FeatureFlags.readBoolean(cached, FeatureFlags.KEY_DEV_MODE, FeatureFlags.DEFAULT_DEV_MODE)

    fun readRegion(): Boolean =
        local?.getBoolean(FeatureFlags.KEY_REGION, FeatureFlags.DEFAULT_REGION)
            ?: FeatureFlags.readBoolean(cached, FeatureFlags.KEY_REGION, FeatureFlags.DEFAULT_REGION)

    fun readCountry(): String =
        local?.getString(FeatureFlags.KEY_COUNTRY, FeatureFlags.DEFAULT_COUNTRY)
            ?: FeatureFlags.readCountry(cached)

    fun setDevMode(on: Boolean) {
        runCatching { local?.edit()?.putBoolean(FeatureFlags.KEY_DEV_MODE, on)?.apply() }
        runCatching { cached?.edit()?.putBoolean(FeatureFlags.KEY_DEV_MODE, on)?.apply() }
    }

    fun setRegion(on: Boolean) {
        runCatching { local?.edit()?.putBoolean(FeatureFlags.KEY_REGION, on)?.apply() }
        runCatching { cached?.edit()?.putBoolean(FeatureFlags.KEY_REGION, on)?.apply() }
    }

    fun setCountry(code: String) {
        runCatching { local?.edit()?.putString(FeatureFlags.KEY_COUNTRY, code)?.apply() }
        runCatching { cached?.edit()?.putString(FeatureFlags.KEY_COUNTRY, code)?.apply() }
    }

    private fun syncLocalToRemote() {
        val l = local ?: return
        val r = cached ?: return
        runCatching {
            r.edit()
                .putBoolean(FeatureFlags.KEY_DEV_MODE, l.getBoolean(FeatureFlags.KEY_DEV_MODE, FeatureFlags.DEFAULT_DEV_MODE))
                .putBoolean(FeatureFlags.KEY_REGION, l.getBoolean(FeatureFlags.KEY_REGION, FeatureFlags.DEFAULT_REGION))
                .putString(FeatureFlags.KEY_COUNTRY, l.getString(FeatureFlags.KEY_COUNTRY, FeatureFlags.DEFAULT_COUNTRY))
                .apply()
        }
    }
}
