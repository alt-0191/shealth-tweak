package com.dev.alt0191.shealthtweak.hook

import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Method


internal fun ClassLoader.loadOrNull(name: String): Class<*>? = try {
    loadClass(name)
} catch (_: Throwable) {
    null
}

internal fun Class<*>.methodOrNull(name: String, vararg params: Class<*>): Method? = try {
    if (params.isEmpty()) {
        declaredMethods.firstOrNull { it.name == name }?.also { it.isAccessible = true }
    } else {
        getDeclaredMethod(name, *params).also { it.isAccessible = true }
    }
} catch (_: Throwable) {
    null
}

internal fun XposedInterface.returnConstant(origin: Method, value: Any?) {
    val iface = this
    hook(origin)
        .setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE)
        .intercept(XposedInterface.Hooker { chain ->
            iface.log(android.util.Log.INFO, "SHealthTweak", "returnConstant hit: " + origin.name + " -> " + value)
            value
        })
}

internal fun XposedInterface.shortCircuit(origin: Method, block: (XposedInterface.Chain) -> Any?) {
    val iface = this
    hook(origin)
        .setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE)
        .intercept(XposedInterface.Hooker { chain ->
            iface.log(android.util.Log.INFO, "SHealthTweak", "shortCircuit hit: " + origin.name)
            block(chain)
        })
}
