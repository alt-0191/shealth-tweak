package com.dev.alt0191.shealthtweak.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.alt0191.shealthtweak.BuildConfig
import com.dev.alt0191.shealthtweak.FeatureState
import com.dev.alt0191.shealthtweak.R
import com.dev.alt0191.shealthtweak.SettingsPrefs


/**
 * 设置页：外观 / 通用 / 关于。样式参考主流 Xposed 模块（LSPosed、TimMonet 等）。
 */
@Composable
fun SettingsScreen() {
    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {

        Section(stringResource(R.string.section_appearance)) {
            DarkModeRow()
            LanguageRow()
            SwitchRow(
                title = stringResource(R.string.dynamic_color),
                desc = stringResource(R.string.dynamic_color_desc),
                checked = SettingsPrefs.dynamicColor,
                onChange = { SettingsPrefs.setDynamicColor(it) },
            )
        }

        Section(stringResource(R.string.section_general)) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.restore_default),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        stringResource(R.string.restore_default_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                var resetDone by remember { mutableStateOf(false) }
                TextButton(onClick = { FeatureState.reset(); resetDone = true }) {
                    Text(if (resetDone) stringResource(R.string.reset_done) else stringResource(R.string.reset))
                }
            }
        }

        Section(stringResource(R.string.section_about)) {
            InfoRow(stringResource(R.string.label_version), "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            InfoRow(stringResource(R.string.label_author), "alt0191", url = "https://github.com/alt-0191")
            InfoRow(stringResource(R.string.label_source), "github.com/alt-0191/shealth-tweak", url = "https://github.com/alt-0191/shealth-tweak")
            InfoRow(stringResource(R.string.label_license), "MIT")
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LanguageRow() {
    val activity = LocalContext.current as? android.app.Activity
    val options = listOf(
        stringResource(R.string.lang_system),
        stringResource(R.string.lang_zh),
        stringResource(R.string.lang_en),
    )
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(
            stringResource(R.string.label_language),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(10.dp))
        ButtonGroup(
            overflowIndicator = { },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEachIndexed { i, label ->
                toggleableItem(
                    checked = SettingsPrefs.language == i,
                    label = label,
                    onCheckedChange = {
                        SettingsPrefs.setLanguage(i)
                        activity?.recreate()
                    },
                    weight = 1f,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DarkModeRow() {
    val options = listOf(stringResource(R.string.dark_system), stringResource(R.string.dark_light), stringResource(R.string.dark_dark))
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(
            stringResource(R.string.dark_mode),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(10.dp))
        ButtonGroup(
            overflowIndicator = { },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEachIndexed { i, label ->
                toggleableItem(
                    checked = SettingsPrefs.darkMode == i,
                    label = label,
                    onCheckedChange = { SettingsPrefs.setDarkMode(i) },
                    weight = 1f,
                )
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 18.dp, bottom = 8.dp),
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            content()
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    desc: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun InfoRow(label: String, value: String, url: String? = null) {
    val uriHandler = LocalUriHandler.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(
                if (url != null) Modifier.clickable { runCatching { uriHandler.openUri(url) } }
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(72.dp),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            color = if (url != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}
