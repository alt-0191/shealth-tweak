package com.dev.alt0191.shealthtweak
internal object ModulePrefsBridge {
    fun readDevMode() = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.readDevMode()
    fun readRegion() = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.readRegion()
    fun readCountry() = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.readCountry()
    fun setDevMode(on: Boolean) = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.setDevMode(on)
    fun setRegion(on: Boolean) = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.setRegion(on)
    fun setCountry(code: String) = com.dev.alt0191.shealthtweak.prefs.ModulePrefs.setCountry(code)
}
