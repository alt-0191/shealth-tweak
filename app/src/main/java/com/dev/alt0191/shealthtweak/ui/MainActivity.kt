package com.dev.alt0191.shealthtweak.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.alt0191.shealthtweak.FeatureState
import com.dev.alt0191.shealthtweak.ModuleStatus
import com.dev.alt0191.shealthtweak.R
import com.dev.alt0191.shealthtweak.SettingsPrefs
import com.dev.alt0191.shealthtweak.prefs.ModulePrefs
import com.dev.alt0191.shealthtweak.ui.theme.AppTheme


/**
 * 模块 GUI（Material 3 Expressive）。
 *
 * 视觉：**加粗大标题 + 饱和主色**，`ButtonGroup` 页面切换，信息卡用分隔线分栏。
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsPrefs.init(this)
        applyLanguage()
        ModulePrefs.start(this)
        FeatureState.load()
        ModuleStatus.refresh()
        setContent { AppTheme { SHealthTweakApp() } }
    }
}
private fun android.app.Activity.applyLanguage() {
    val lang = when (com.dev.alt0191.shealthtweak.SettingsPrefs.language) {
        com.dev.alt0191.shealthtweak.SettingsPrefs.LANG_ZH -> "zh"
        com.dev.alt0191.shealthtweak.SettingsPrefs.LANG_EN -> "en"
        else -> ""
    }
    if (lang.isEmpty()) return
    val locale = java.util.Locale(lang)
    java.util.Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    @Suppress("DEPRECATION")
    resources.updateConfiguration(config, resources.displayMetrics)
}

private val TABS = listOf("功能", "设置")

@Composable
private fun SHealthTweakApp() {
    var tab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Header()
        TabSwitcher(selected = tab, onSelect = { tab = it })
        Spacer(Modifier.height(4.dp))
        when (tab) {
            0 -> FeaturesScreen()
            else -> SettingsScreen()
        }
        Spacer(Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Header() {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                ModuleStatusRow()
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}
@Composable
private fun ModuleStatusRow() {
    val activated = ModuleStatus.isActivated
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(8.dp)
                .background(
                    if (activated) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.small,
                )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            if (activated) stringResource(R.string.status_activated)
            else stringResource(R.string.status_not_activated),
            style = MaterialTheme.typography.labelLarge,
            color = if (activated) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun TabSwitcher(selected: Int, onSelect: (Int) -> Unit) {
    val labels = TABS.map { if (it == "功能") stringResource(R.string.tab_features) else stringResource(R.string.tab_settings) }
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        labels.forEachIndexed { i, label ->
            SegmentedButton(
                selected = selected == i,
                onClick = { onSelect(i) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = labels.size),
                icon = { SegmentedButtonDefaults.Icon(active = selected == i) },
            ) { Text(label) }
        }
    }
}
