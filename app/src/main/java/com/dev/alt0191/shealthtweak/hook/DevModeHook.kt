package com.dev.alt0191.shealthtweak.hook

import android.content.Context
import android.widget.RadioGroup
import io.github.libxposed.api.XposedInterface


object DevModeHook {

    private const val FEATURE_MANAGER = "com.samsung.android.app.shealth.config.FeatureManager"
    private const val SDK_DEV_POLICY =
        "com.samsung.android.sdk.health.sdkpolicy.SdkDevPolicyControl\$Companion"
    private const val FEATURE_LIST_ACTIVITY =
        "com.samsung.android.app.shealth.config.FeatureListActivity"
    private const val VERIFICATION_DIALOG =
        "com.samsung.android.app.shealth.config.VerificationDialogActivity"
    private const val VERIFICATION_UTILS =
        "com.samsung.android.app.shealth.config.FeatureVerificationUtils"

    fun install(api: XposedInterface, cl: ClassLoader) {
        val fm = cl.loadOrNull(FEATURE_MANAGER)

        // C1() gates the SetFeatures entry on isDeveloperModeOn && isEnabled(); both must be true.
        if (fm != null) {
            fm.methodOrNull("isEnabled")?.let { api.returnConstant(it, true) }
            fm.methodOrNull("isCscFeatureEnabled")?.let { api.returnConstant(it, true) }
        }

        cl.loadOrNull(SDK_DEV_POLICY)
            ?.methodOrNull("isDeveloperModeOn", Context::class.java)
            ?.let { api.returnConstant(it, true) }

        // VerificationDialogActivity.onCreate short-circuits on isVerified(): setResult(RESULT_OK); finish();
        cl.loadOrNull(VERIFICATION_UTILS)
            ?.methodOrNull("isVerified")
            ?.let { api.returnConstant(it, true) }

        if (fm != null) {
            cl.loadOrNull(FEATURE_LIST_ACTIVITY)
                ?.methodOrNull("handleDeveloperModeEnable", String::class.java, RadioGroup::class.java)
                ?.let { origin ->
                    api.shortCircuit(origin) { chain ->
                        val key = chain.getArg(0) as? String
                        if (key != null) {
                            runCatching {
                                val inst = fm.methodOrNull("getInstance")?.invoke(null)
                                fm.methodOrNull("setStringValue", String::class.java, String::class.java)
                                    ?.invoke(inst, key, "on")
                            }
                        }
                        null
                    }
                }
        }
    }
}
