package com.dev.alt0191.shealthtweak

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


object FeatureState {

    private val _devMode = mutableStateOf(FeatureFlags.DEFAULT_DEV_MODE)
    private val _region = mutableStateOf(FeatureFlags.DEFAULT_REGION)
    private val _country = mutableStateOf(FeatureFlags.DEFAULT_COUNTRY)

    val devMode: Boolean get() = _devMode.value
    val region: Boolean get() = _region.value
    val country: String get() = _country.value

    fun load() {
        _devMode.value = ModulePrefsBridge.readDevMode()
        _region.value = ModulePrefsBridge.readRegion()
        _country.value = ModulePrefsBridge.readCountry()
    }

    fun setDevMode(on: Boolean) {
        _devMode.value = on
        ModulePrefsBridge.setDevMode(on)
    }

    fun setRegion(on: Boolean) {
        _region.value = on
        ModulePrefsBridge.setRegion(on)
    }

    fun setCountry(code: String) {
        _country.value = code
        ModulePrefsBridge.setCountry(code)
    }

    fun reset() {
        setDevMode(FeatureFlags.DEFAULT_DEV_MODE)
        setRegion(FeatureFlags.DEFAULT_REGION)
        setCountry(FeatureFlags.DEFAULT_COUNTRY)
    }
}
