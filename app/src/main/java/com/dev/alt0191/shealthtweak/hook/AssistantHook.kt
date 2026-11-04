package com.dev.alt0191.shealthtweak.hook

import io.github.libxposed.api.XposedInterface

/** Samsung Health 7.1.9.019: flags consumed by VarUtils (s490.b) and entry UI. */
object AssistantHook {
    private val overrides = mapOf(
        "ai_assistant_entry_point_enabled" to true,
        "ai_assistant_disabled" to false,
        "ai_assistant_model_check" to false,
        "ai_assistant_language_location_check" to false,
    )

    fun install(api: XposedInterface, cl: ClassLoader, enabled: Boolean) {
        val fm = cl.loadOrNull("com.samsung.android.app.shealth.config.FeatureManager")
            ?: error("FeatureManager missing")
        // Override feature accessors rather than the obfuscated eligibility method.
        // Account checks and the normal consent/onboarding flow still run.
        for (name in listOf("getBooleanValue", "isSupported")) {
            val method = fm.getDeclaredMethod(name, String::class.java)
            check(method.returnType == Boolean::class.javaPrimitiveType) {
                "Unexpected FeatureManager.$name return type"
            }
            method.isAccessible = true
            api.shortCircuit(method) { chain ->
                val key = chain.getArg(0) as? String
                if (enabled) overrides[key] ?: chain.proceed()
                else when (key) {
                    "ai_assistant_entry_point_enabled" -> false
                    "ai_assistant_disabled" -> true
                    else -> chain.proceed()
                }
            }
        }
        if (enabled) {
            fm.getDeclaredMethod("getStringValue", String::class.java).let { method ->
                api.shortCircuit(method) { chain ->
                    when (chain.getArg(0)) {
                        "ai_assistant_server_env" -> "stg"
                        "ai_assistant_server_endpoint" -> ""
                        else -> chain.proceed()
                    }
                }
            }
        }
        api.log(android.util.Log.INFO, "SHealthTweak", "Assistant hooks installed: enabled=$enabled")
    }
}
