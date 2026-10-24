package com.dev.alt0191.shealthtweak

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
object SettingsPrefs {

    private const val FILE = "ui_settings"
    private const val KEY_DARK = "dark_mode"
    private const val KEY_DYNAMIC = "dynamic_color"

    const val DARK_SYSTEM = 0
    const val DARK_LIGHT = 1
    const val DARK_DARK = 2

    const val LANG_SYSTEM = 0
    const val LANG_ZH = 1
    const val LANG_EN = 2

    private val _darkMode = mutableIntStateOf(DARK_SYSTEM)
    private val _dynamicColor = mutableStateOf(true)
    private val _language = mutableIntStateOf(LANG_SYSTEM)

    val darkMode: Int get() = _darkMode.intValue
    val dynamicColor: Boolean get() = _dynamicColor.value
    val language: Int get() = _language.intValue

    private var sp: SharedPreferences? = null

    fun init(context: Context) {
        if (sp != null) return
        sp = context.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        _darkMode.intValue = sp?.getInt(KEY_DARK, DARK_SYSTEM) ?: DARK_SYSTEM
        _dynamicColor.value = sp?.getBoolean(KEY_DYNAMIC, true) ?: true
        _language.intValue = sp?.getInt("language", LANG_SYSTEM) ?: LANG_SYSTEM
    }

    fun setDarkMode(mode: Int) {
        _darkMode.intValue = mode
        sp?.edit()?.putInt(KEY_DARK, mode)?.apply()
    }

    fun setDynamicColor(on: Boolean) {
        _dynamicColor.value = on
        sp?.edit()?.putBoolean(KEY_DYNAMIC, on)?.apply()
    }

    fun setLanguage(v: Int) {
        _language.intValue = v
        sp?.edit()?.putInt("language", v)?.apply()
    }
}
