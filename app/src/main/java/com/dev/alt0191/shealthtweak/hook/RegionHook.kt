package com.dev.alt0191.shealthtweak.hook

import android.content.Context
import com.dev.alt0191.shealthtweak.FeatureFlags
import io.github.libxposed.api.XposedInterface

object RegionHook {

    private const val FEATURE_MANAGER = "com.samsung.android.app.shealth.config.FeatureManager"
    private const val ACCOUNT_OPERATION =
        "com.samsung.android.sdk.healthdata.privileged.AccountOperation"
    private const val ACCOUNT_UTIL =
        "com.samsung.android.sdk.healthdata.privileged.util.AccountUtil"

    // Real Feature keys
    private const val KEY_CSC = "Common_csc"
    private const val KEY_MCC = "Common_mcc"
    private const val KEY_CSC_UPPER = "COMMON_CSC"
    private const val KEY_MCC_UPPER = "COMMON_MCC"
    private const val KEY_SA_COUNTRY = "samsung_account_country_code"
    private const val KEY_SA_COUNTRY3 = "samsung_account_three_digit_country_code"

    fun install(api: XposedInterface, cl: ClassLoader, country: FeatureFlags.Country) {
        val fm = cl.loadOrNull(FEATURE_MANAGER)

        runCatching {
            val Locale = Class.forName("java.util.Locale")
            Locale.getDeclaredMethod("getCountry").let { m -> api.shortCircuit(m) { country.iso2 } }
            Locale.getDeclaredMethod("getISO3Country").let { m -> api.shortCircuit(m) { country.iso3 } }
        }
        runCatching {
            val TM = Class.forName("android.telephony.TelephonyManager")
            TM.getDeclaredMethod("getSimCountryIso").let { m -> api.shortCircuit(m) { country.iso2 } }
            TM.getDeclaredMethod("getNetworkCountryIso").let { m -> api.shortCircuit(m) { country.iso2 } }
        }

        // kq3.a(Context) is the getCountryCode source used by region gating
        cl.loadOrNull("kq3")
            ?.methodOrNull("a", Context::class.java)
            ?.let { api.shortCircuit(it) { country.iso2 } }

        if (fm != null) {
            fm.methodOrNull("getStringValue", String::class.java)?.let { origin ->
                api.shortCircuit(origin) { chain ->
                    when (chain.getArg(0) as? String) {
                        KEY_CSC, KEY_CSC_UPPER -> country.csc
                        KEY_MCC, KEY_MCC_UPPER -> country.mcc
                        KEY_SA_COUNTRY -> country.iso2
                        KEY_SA_COUNTRY3 -> country.iso3
                        else -> chain.proceed()
                    }
                }
            }
        }

        cl.loadOrNull(ACCOUNT_UTIL)
            ?.methodOrNull("isChina")
            ?.let { api.shortCircuit(it) { country.isChina } }

        cl.loadOrNull(ACCOUNT_OPERATION)
            ?.methodOrNull("isChinaConsentGranted", Context::class.java)
            ?.let { api.returnConstant(it, true) }

        writeFeaturePref(cl, country)
    }

    private fun writeFeaturePref(cl: ClassLoader, country: FeatureFlags.Country) {
        Thread {
            repeat(30) {
                runCatching {
                    val holderCls = cl.loadOrNull("com.samsung.android.app.shealth.app.helper.ContextHolder")
                    val ctx = holderCls?.methodOrNull("getContext")?.invoke(null) as? Context
                    if (ctx != null) {
                        val pref = ctx.getSharedPreferences("final_feature_list_pref", Context.MODE_PRIVATE)
                        pref.edit()
                            .putString(KEY_CSC, country.csc)
                            .putString(KEY_MCC, country.mcc)
                            .putString(KEY_CSC_UPPER, country.csc)
                            .putString(KEY_MCC_UPPER, country.mcc)
                            .putString(KEY_SA_COUNTRY, country.iso2)
                            .putString(KEY_SA_COUNTRY3, country.iso3)
                            .apply()
                        return@Thread
                    }
                }
                Thread.sleep(500)
            }
        }.start()
    }
}
