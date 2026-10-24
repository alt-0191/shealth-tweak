package com.dev.alt0191.shealthtweak.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.alt0191.shealthtweak.FeatureFlags
import com.dev.alt0191.shealthtweak.FeatureState
import com.dev.alt0191.shealthtweak.R
import com.dev.alt0191.shealthtweak.prefs.ModulePrefs

/**
 * 功能页：两个开关。
 *  - 开发者模式（默认开启）
 *  - 地区伪装（默认关闭，开启后选国家）
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FeaturesScreen() {
    val devMode = FeatureState.devMode
    val region = FeatureState.region
    val country = FeatureState.country
    val motionScheme = MaterialTheme.motionScheme
    val reduceMotion = androidx.compose.ui.platform.LocalAccessibilityManager.current != null && androidx.compose.ui.platform.LocalGraphicsContext.current == null

    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {

        if (!ModulePrefs.isAvailable) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                LoadingIndicator(Modifier.height(36.dp))
                Spacer(Modifier.width(16.dp))
                Text(
                    stringResource(R.string.loading_framework),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        FeatureCard(
            feature = FeatureFlags.DEV_MODE,
            checked = devMode,
            onCheckedChange = { FeatureState.setDevMode(it) },
        ) {}

        FeatureCard(
            feature = FeatureFlags.REGION,
            checked = region,
            onCheckedChange = { FeatureState.setRegion(it) },
        ) {
            AnimatedVisibility(
                visible = region,
                enter = slideInVertically(animationSpec = motionScheme.defaultSpatialSpec()) + fadeIn(animationSpec = motionScheme.defaultEffectsSpec()),
                exit = slideOutVertically(animationSpec = motionScheme.defaultSpatialSpec()) + fadeOut(animationSpec = motionScheme.defaultEffectsSpec()),
            ) {
                CountryPicker(
                    selected = country,
                    onSelect = { FeatureState.setCountry(it) },
                )
            }
        }

        RestartCard()
    }
}

@Composable
private fun RestartCard() {
    val context = LocalContext.current
    var restarting by remember { mutableStateOf(false) }
    fun doRestart() {
        runCatching {
            val sh = Runtime.getRuntime()
            sh.exec(arrayOf("sh", "-c", "su -c 'am force-stop " + FeatureFlags.TARGET_PACKAGE + "'"))
            Thread {
                Thread.sleep(1500)
                runCatching {
                    val am = context.getSystemService(android.content.Context.ACTIVITY_SERVICE)
                            as android.app.ActivityManager
                    am.killBackgroundProcesses(FeatureFlags.TARGET_PACKAGE)
                }
                runCatching {
                    val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(android.net.Uri.parse("package:" + FeatureFlags.TARGET_PACKAGE))
                        .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                Thread.sleep(800)
                android.os.Handler(android.os.Looper.getMainLooper()).post { restarting = false }
            }.start()
        }
    }

    Button(
        onClick = {
            if (restarting) return@Button
            restarting = true
            doRestart()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .height(56.dp),
        shape = MaterialTheme.shapes.large,
        enabled = !restarting,
    ) {
        Text(
            if (restarting) stringResource(R.string.restarting) else stringResource(R.string.restart_apply),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
@Composable
private fun FeatureCard(
    feature: FeatureFlags.Feature,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    extra: @Composable () -> Unit,
) {
    val motionScheme = MaterialTheme.motionScheme
    val reduceMotion = androidx.compose.ui.platform.LocalAccessibilityManager.current != null && androidx.compose.ui.platform.LocalGraphicsContext.current == null
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
    Text(
                        if (feature.titleRes != 0) stringResource(feature.titleRes) else feature.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(4.dp))
    Text(
                        if (feature.descRes != 0) stringResource(feature.descRes) else feature.desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Switch(checked = checked, onCheckedChange = onCheckedChange)
            }
            AnimatedVisibility(
                visible = checked,
                enter = slideInVertically(animationSpec = motionScheme.defaultSpatialSpec()) + fadeIn(animationSpec = motionScheme.defaultEffectsSpec()),
                exit = slideOutVertically(animationSpec = motionScheme.defaultSpatialSpec()) + fadeOut(animationSpec = motionScheme.defaultEffectsSpec()),
            ) {
                extra()
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CountryPicker(selected: String, onSelect: (String) -> Unit) {
    Column(Modifier.padding(top = 14.dp)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(14.dp))
        Text(
            stringResource(R.string.label_account_region),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FeatureFlags.COUNTRIES.forEach { c ->
                FilterChip(
                    selected = selected == c.code,
                    onClick = { onSelect(c.code) },
                    label = { Text(if (c.resId != 0) stringResource(c.resId) else c.label) },
                )
            }
        }
    }
}

