package com.Tofwure2.Kestub.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.di.AppViewModelFactory
import com.Tofwure2.Kestub.core.theme.AlarmRed
import com.Tofwure2.Kestub.core.theme.MintInk
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.SurfaceAlt
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.theme.TextTertiary
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.VaultBackground
import com.Tofwure2.Kestub.domain.model.ChainSpeed
import com.Tofwure2.Kestub.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    var confirmVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0C0C11).copy(alpha = 0.92f),
                Color(0xFF15151C).copy(alpha = 0.88f),
                Color(0xFF0A0A0E).copy(alpha = 0.95f)
            )
        ),
        artAlpha = 0.20f,
        moteCount = 8,
        moteSeed = 91
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.settings_title), onHome = onMenu)
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpeedOption(
                    current = state.settings.chainSpeed,
                    onSelect = { viewModel.setChainSpeed(it) }
                )
                SwitchOption(
                    label = stringResource(R.string.setting_high_contrast),
                    checked = state.settings.highContrast,
                    onToggle = { viewModel.toggleHighContrast() }
                )
                SwitchOption(
                    label = stringResource(R.string.setting_haptics),
                    checked = state.settings.haptics,
                    onToggle = { viewModel.toggleHaptics() }
                )
                SwitchOption(
                    label = stringResource(R.string.setting_hint),
                    checked = state.settings.persistentHint,
                    onToggle = { viewModel.toggleHint() }
                )
                SecondaryButton(
                    text = stringResource(R.string.reset_progress),
                    accent = AlarmRed,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { confirmVisible = true }
                )
            }
            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                modifier = Modifier.fillMaxWidth(),
                onClick = onMenu
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
    if (confirmVisible) {
        ConfirmResetDialog(
            onConfirm = {
                viewModel.resetProgress()
                confirmVisible = false
            },
            onCancel = { confirmVisible = false }
        )
    }
}

@Composable
private fun SpeedOption(
    current: ChainSpeed,
    onSelect: (ChainSpeed) -> Unit
) {
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.setting_chain_speed),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                color = TextPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpeedChip(
                    label = stringResource(R.string.speed_relaxed),
                    selected = current == ChainSpeed.RELAXED,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(ChainSpeed.RELAXED) }
                )
                SpeedChip(
                    label = stringResource(R.string.speed_standard),
                    selected = current == ChainSpeed.STANDARD,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(ChainSpeed.STANDARD) }
                )
                SpeedChip(
                    label = stringResource(R.string.speed_swift),
                    selected = current == ChainSpeed.SWIFT,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(ChainSpeed.SWIFT) }
                )
            }
        }
    }
}

@Composable
private fun SpeedChip(
    label: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .background(
                if (selected) MintPrimary else Color.White.copy(alpha = 0.05f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (selected) MintPrimary else Color.White.copy(alpha = 0.12f),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = if (selected) MintInk else TextSecondary
        )
    }
}

@Composable
private fun SwitchOption(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val onLabel = stringResource(R.string.setting_on)
    val offLabel = stringResource(R.string.setting_off)
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .semantics { stateDescription = if (checked) onLabel else offLabel },
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MintInk,
                    checkedTrackColor = MintPrimary,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = SurfaceAlt
                )
            )
        }
    }
}
