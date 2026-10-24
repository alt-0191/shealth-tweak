package com.dev.alt0191.shealthtweak

import android.content.SharedPreferences

object FeatureFlags {

    const val TARGET_PACKAGE = "com.sec.android.app.shealth"
    const val GROUP = "features"

    const val KEY_DEV_MODE = "dev_mode"
    const val KEY_REGION = "region"
    const val KEY_COUNTRY = "country"

    const val DEFAULT_DEV_MODE = true
    const val DEFAULT_REGION = false
    const val DEFAULT_COUNTRY = "KR"

    data class Feature(
        val key: String,
        val title: String,
        val desc: String,
        val defaultOn: Boolean,
        val titleRes: Int = 0,
        val descRes: Int = 0,
    )

    val DEV_MODE = Feature(
        KEY_DEV_MODE, "开发者模式",
        "解锁 FeatureList 全部调试功能，跳过开发者密钥验证",
        DEFAULT_DEV_MODE,
        titleRes = R.string.feature_dev_mode,
        descRes = R.string.feature_dev_mode_desc,
    )

    val REGION = Feature(
        KEY_REGION, "地区伪装",
        "把地区改写为所选地区，解除地区限制",
        DEFAULT_REGION,
        titleRes = R.string.feature_region,
        descRes = R.string.feature_region_desc,
    )

    val ALL = listOf(DEV_MODE, REGION)

    data class Country(
        val code: String,
        val csc: String,
        val mcc: String,
        val label: String,
        val resId: Int = 0,
        val isChina: Boolean = false,
        val iso2: String = code,
        val iso3: String = code,
    )

    val COUNTRIES: List<Country> = listOf(
        Country("KR", "KR", "450", "韩国", resId = R.string.region_kr, iso3 = "KOR"),
        Country("CN", "CN", "460", "中国", resId = R.string.region_cn, isChina = true, iso3 = "CHN"),
        Country("JP", "JP", "440", "日本", resId = R.string.region_jp, iso3 = "JPN"),
        Country("TW", "TW", "466", "台湾", resId = R.string.region_tw, iso3 = "TWN"),
        Country("HK", "HK", "454", "香港", resId = R.string.region_hk, iso3 = "HKG"),
        Country("US", "US", "310", "美国", resId = R.string.region_us, iso3 = "USA"),
        Country("GB", "GB", "234", "英国", resId = R.string.region_gb, iso3 = "GBR"),
        Country("DE", "DE", "262", "德国", resId = R.string.region_de, iso3 = "DEU"),
        Country("FR", "FR", "208", "法国", resId = R.string.region_fr, iso3 = "FRA"),
        Country("IN", "IN", "404", "印度", resId = R.string.region_in, iso3 = "IND"),
        Country("SG", "SG", "525", "新加坡", resId = R.string.region_sg, iso3 = "SGP"),
        Country("AU", "AU", "505", "澳大利亚", resId = R.string.region_au, iso3 = "AUS"),
    )

    fun countryOf(code: String?): Country =
        COUNTRIES.firstOrNull { it.code == code } ?: COUNTRIES.first()

    fun readBoolean(prefs: SharedPreferences?, key: String, default: Boolean): Boolean = try {
        prefs?.getBoolean(key, default) ?: default
    } catch (_: Throwable) {
        default
    }

    fun readCountry(prefs: SharedPreferences?): String = try {
        prefs?.getString(KEY_COUNTRY, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
    } catch (_: Throwable) {
        DEFAULT_COUNTRY
    }
}
