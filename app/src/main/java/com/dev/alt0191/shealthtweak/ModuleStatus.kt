package com.dev.alt0191.shealthtweak

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue


object ModuleStatus {

    const val STATE_UNKNOWN = 0
    const val STATE_ACTIVATED = 1
    const val STATE_NOT_BOUND = 2

    var state by mutableIntStateOf(STATE_UNKNOWN)
        private set

    // Activated = the Xposed framework service is bound and the target package is in scope.
    fun refresh(service: io.github.libxposed.service.XposedService? = null) {
        val svc = service ?: com.dev.alt0191.shealthtweak.prefs.ModulePrefs.serviceOrNull()
        if (svc == null) {
            state = STATE_NOT_BOUND
            return
        }
        val activated = runCatching {
            svc.scope.contains(FeatureFlags.TARGET_PACKAGE)
        }.getOrDefault(false)
        state = if (activated) STATE_ACTIVATED else STATE_NOT_BOUND
    }

    val isActivated: Boolean get() = state == STATE_ACTIVATED
}
